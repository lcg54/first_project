package com.myproject.dao;

import com.myproject.bean.Member;
import com.myproject.bean.Usage;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class UsageDao extends SuperDao {
    public UsageDao() {
    }

    private Usage preUsage(ResultSet rs) {
        Usage usage = null;
        try {
            usage = new Usage();
            usage.setId(rs.getString("id"));
            usage.setTelecom(rs.getString("telecom"));
            usage.setPlan(rs.getString("plan"));
            usage.setUsedData(rs.getDouble("used_data"));
            usage.setGrade(rs.getString("grade"));
            usage.setDiscountRate(rs.getInt("discount_rate"));
            usage.setAmount(rs.getInt("amount"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usage;
    }

    public double dataOfPlan(String plan) {
        double dp = 0;

        switch (plan) {
            case "5G 프리미엄": dp = 33.00; break;
            case "5G 스탠다드": dp = 15.00; break;
            case "LTE 무제한": dp = 30.00; break;
            case "LTE 라이트": dp = 6.00; break;
        }
        return dp;
    }

    public int amountOfPlan(String plan) {
        int ap = 0;

        switch (plan) {
            case "5G 프리미엄": ap = 89000; break;
            case "5G 스탠다드": ap = 55000; break;
            case "LTE 무제한": ap = 69000; break;
            case "LTE 라이트": ap = 33000; break;
        }
        return ap;
    }

    public int calcAgeDiscount(int age) {
        int ad = 0;

        if (age <= 19 || age >= 65) {
            ad = 30;
        }
        return ad;
    }

    public String calcGrade(LocalDate joinDate) {
        String grade = null;
        // 가입일로부터 오늘은 몇 년 지났는가
        long yearsBetween = ChronoUnit.YEARS.between(joinDate, LocalDate.now());

        if (yearsBetween <= 2) {
            grade = "normal";
        } else if (yearsBetween <= 5) {
            grade = "vip";
        } else {
            grade = "vvip";
        }
        return grade;
    }

    public int calcGradeDiscount(String grade) {
        int gd = 0;

        switch (grade) {
            case "normal": gd = 0; break;
            case "vip": gd = 10; break;
            case "vvip": gd = 20; break;
        }
        return gd;
    }

    public int calcDiscount(int age, String grade) {
        int ageDiscount = calcAgeDiscount(age);
        int gradeDiscount = calcGradeDiscount(grade);
        return ageDiscount + gradeDiscount;
    }

    public int calcAmount(String plan, int discountRate) {
        int base = amountOfPlan(plan);
        return (int) (base * (1 - discountRate / 100.0));
    }


    public int signUp(Usage u) {
        int res = 0;

        String sql = "insert into usage (id, telecom, plan, used_data, grade, discount_rate, amount)" +
                " values (?, ?, ?, 0.00, ?, ?, ?)";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, u.getId());
            pstmt.setString(2, u.getTelecom());
            pstmt.setString(3, u.getPlan());
            pstmt.setString(4, u.getGrade());
            pstmt.setInt(5, u.getDiscountRate());
            pstmt.setInt(6, u.getAmount());

            res = pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public Usage callById(String loggedInId) {
        Usage u = null;

        String sql = "select * from usage where id = ?";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, loggedInId);

            try (ResultSet rs = pstmt.executeQuery();) {
                if (rs.next()) {
                    u = this.preUsage(rs);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return u;
    }

    public int updatePlan(String loggedInId, String plan) {
        int res = 0;
        Usage u = this.callById(loggedInId);

        String sql = "update usage set plan = ?, amount = ? where id = ?";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, plan);
            // 요금제 변경 시 최종 납부액도 변경
            pstmt.setInt(2, this.calcAmount(plan, u.getDiscountRate()));
            pstmt.setString(3, loggedInId);
            res = pstmt.executeUpdate();
            conn.commit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public int updateTel(String loggedInId, String tel) {
        int res = 0;
        String sql = "update usage set telecom = ? where id = ?";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tel);
            pstmt.setString(2, loggedInId);
            res = pstmt.executeUpdate();
            conn.commit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public int deleteOne(String loggedInId) {
        int res = 0;

        String sql = "delete from usage where id = ?";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loggedInId);
            res = pstmt.executeUpdate();
            conn.commit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }
}
