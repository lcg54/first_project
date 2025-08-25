package com.myproject.dao;

import com.myproject.bean.Member;
import com.myproject.bean.Usage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsageDao extends SuperDao{
    public UsageDao() {
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

    public int amountPlan(String loggedInId) {
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

    public int calcDiscount_rate(String loggedInId) {
        MemberDao mdao = new MemberDao();
        Member m = mdao.callById(loggedInId);
        int age = m.getAge();
        int ageDiscount = 0;

        if (age <= 19 || age >= 65) {
            ageDiscount = 30;
        }

        Usage u = this.callById(loggedInId);
        String grade = u.getGrade();
        int gradeDiscount = 0;

        switch (grade) {
            case "normal": gradeDiscount = 0; break;
            case "vip": gradeDiscount = 10; break;
            case "vvip": gradeDiscount = 20; break;
        }

        int discount_rate = ageDiscount + gradeDiscount;
        return discount_rate;
    }

    public int calcAmount(String loggedInId) {
        int base = amountPlan(loggedInId);
        int discount = calcDiscount_rate(loggedInId);
        int amount = (int)(base * (1 - discount / 100.0));
        return amount;
    }
// SELECT u.plan, u.grade, m.age FROM usage u JOIN members m ON u.id = m.id WHERE u.id = ? 와 같이 join을 활용해도 되지만, 이러면 불러온 데이터를 저장하는 객체와 일치하는 필드를 가진 bean이 없으니 새 bean을 만들어야 함. 필드 구성이 바뀔 때마다 매번 만들어야 함. 굳이..?



    public Usage callById(String loggedInId) {
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

        String sql = "insert into members (id, telecom, plan, grade, discount_rate, amount) values (?, ?, ?, 'normal', 0, ?)";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, u.getId());
            pstmt.setString(2, u.getTelecom());
            pstmt.setString(3, u.getPlan());
            pstmt.setInt(6, calcAmount(u.getId()));

            res = pstmt.executeUpdate();
            //conn.commit();

        } catch (SQLException e) {
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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

}
