package com.myproject.dao;

import com.myproject.bean.Usage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsageDao extends SuperDao{
    Usage usage = null;
    public UsageDao() {
        usage = new Usage();
    }

    private Usage preUsage(ResultSet rs){
        Usage usage = null;
        try {
            usage = new Usage();
            usage.setId(rs.getString("id"));
            usage.setTelecom(rs.getString("telecom"));
            usage.setPlan(rs.getString("plan"));
            usage.setGrade(rs.getString("grade"));
            usage.setDiscount_rate(rs.getInt("discount_rate"));
            usage.setAmount(rs.getInt("amount"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usage;
    }

    public int calcPlan(String plan) {
        int cp = 0;
        switch (plan) {
            case "5G 프리미엄": cp = 89000;break;
            case "5G 스탠다드": cp = 55000;break;
            case "LTE 무제한": cp = 69000;break;
            case "LTE 라이트": cp = 33000;break;
        }
        usage.setPlan(plan);
        return cp;
    }

    public int calcGrade(String grade) {
        int cg = 0;
        switch (grade) {
            case "normal": cg = 89000;break;
            case "vip": cg = 55000;break;
            case "vvip": cg = 69000;break;
        }
        usage.setGrade(grade);
        return cg;
    }

    public int calcAmount(String plan) {
        int amount = calcPlan(plan) * ((100 - usage.getDiscount_rate()/100));
        usage.setAmount(amount);
        return amount;
    }

    public Usage showMyInfo(String loggedInId) {
        Usage u = null;

        String sql = "select * from usage where id = ?";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);){
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
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public int signUp(Usage u) {
        int res = 0;

        String sql = "insert into members (id, telecom, plan, grade, discount_rate, amount) values (?, ?, ?, normal, 0, ?)";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, u.getId());
            pstmt.setString(2, u.getTelecom());
            pstmt.setString(3, u.getPlan());
            pstmt.setInt(6, calcAmount(u.getPlan()));

            res = pstmt.executeUpdate();
            //conn.commit();

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) { // primary key(=id)가 중복되는 경우 SQLException 발생
                System.out.println("이미 존재하는 ID입니다.");
            }
            throw new RuntimeException(e);
        }
        return res;
    }
}
