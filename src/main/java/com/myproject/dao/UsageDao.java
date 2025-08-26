package com.myproject.dao;

import com.myproject.bean.Member;
import com.myproject.bean.Usage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            case "5G 프리미엄": dp = 25.00; break;
            case "5G 스탠다드": dp = 10.00; break;
            case "LTE 무제한": dp = 30.00; break;
            case "LTE 라이트": dp = 5.00; break;
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
                " values (?, ?, ?, 0.00, 'normal', ?, ?)";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, u.getId());
            pstmt.setString(2, u.getTelecom());
            pstmt.setString(3, u.getPlan());
            pstmt.setInt(6, calcDiscount(u.getId()));
            pstmt.setInt(7, calcAmount(u.getId()));

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

        String sql1 = "select * from usage where id = ? and plan = ?";
        String sql2 = "update usage set plan = ? where id = ?";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt1 = conn.prepareStatement(sql1);) {
            pstmt1.setString(1, loggedInId);
            pstmt1.setString(2, plan);
            ResultSet rs = pstmt1.executeQuery();

            if (!rs.next()) {
                try (PreparedStatement pstmt2 = conn.prepareStatement(sql2);) {
                    pstmt2.setString(2, loggedInId);
                    pstmt2.setString(1, plan);
                    res = pstmt2.executeUpdate();
                    conn.commit();
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public int updateTelecom(String loggedInId, String tel) {
        int res = 0;

        String sql1 = "select * from usage where id = ? and telecom = ?";
        String sql2 = "update usage set telecom = ? where id = ?";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt1 = conn.prepareStatement(sql1);) {
            pstmt1.setString(1, loggedInId);
            pstmt1.setString(2, tel);
            ResultSet rs = pstmt1.executeQuery();

            if (!rs.next()) {
                try (PreparedStatement pstmt2 = conn.prepareStatement(sql2);) {
                    pstmt2.setString(2, loggedInId);
                    pstmt2.setString(1, tel);
                    res = pstmt2.executeUpdate();
                    conn.commit();
                }
            }

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
