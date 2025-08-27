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

    public double dataOfPlan(String loggedInId) {
        Usage u = this.callById(loggedInId);
        String plan = u.getPlan();
        double dp = 0;

        switch (plan) {
            case "5G 프리미엄": dp = 33.00; break;
            case "5G 스탠다드": dp = 15.00; break;
            case "LTE 무제한": dp = 30.00; break;
            case "LTE 라이트": dp = 6.00; break;
        }
        return dp;
    }

    public int amountOfPlan(String loggedInId) {
        Usage u = this.callById(loggedInId);
        String plan = u.getPlan();
        int ap = 0;

        switch (plan) {
            case "5G 프리미엄": ap = 89000; break;
            case "5G 스탠다드": ap = 55000; break;
            case "LTE 무제한": ap = 69000; break;
            case "LTE 라이트": ap = 33000; break;
        }
        return ap;
    }

    public int calcAgeDiscount(String loggedInId) {
        MemberDao mdao = new MemberDao();
        Member m = mdao.callById(loggedInId);
        int age = m.getAge();
        int ageDiscount = 0;

        if (age <= 19 || age >= 65) {
            ageDiscount = 30;
        }
        return ageDiscount;
    }

    public String calcGrade(String loggedInId) {
        MemberDao mdao = new MemberDao();
        Member m = mdao.callById(loggedInId);
        // 가입일로부터 오늘은 몇 년 지났는가
        long yearsBetween = ChronoUnit.YEARS.between(m.getJoinDate(), LocalDate.now());

        if (yearsBetween <= 2) {
            return "normal";
        } else if (yearsBetween <= 5) {
            return "vip";
        } else {
            return "vvip";
        }
    }

    public int calcGradeDiscount(String loggedInId) {
        Usage u = this.callById(loggedInId);
        String grade = u.getGrade();
        int gradeDiscount = 0;

        switch (grade) {
            case "normal": gradeDiscount = 0; break;
            case "vip": gradeDiscount = 10; break;
            case "vvip": gradeDiscount = 20; break;
        }
        return gradeDiscount;
    }

    public int calcDiscount(String loggedInId) {
        int ageDiscount = calcAgeDiscount(loggedInId);
        int gradeDiscount = calcGradeDiscount(loggedInId);
        int discountRate = ageDiscount + gradeDiscount;
        return discountRate;
    }

    public int calcAmount(String loggedInId) {
        int base = amountOfPlan(loggedInId);
        int discount = calcDiscount(loggedInId);
        int amount = (int) (base * (1 - discount / 100.0));
        return amount;
    }


    public int signUp(Usage u) {
        int res = 0;

        String sql = "insert into usage (id, telecom, plan, used_data, grade, discount_rate, amount)" +
                " values (?, ?, ?, 0.00, 'normal', 0, 0)";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, u.getId());
            pstmt.setString(2, u.getTelecom());
            pstmt.setString(3, u.getPlan());
//            pstmt.setString(4, this.calcGrade(u.getId()));
//            pstmt.setInt(5, this.calcDiscount(u.getId()));
//            pstmt.setInt(6, this.calcAmount(u.getId()));

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
        String sql = "update usage set plan = ? where id = ?";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, plan);
            pstmt.setString(2, loggedInId);
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
