package com.myproject.dao;

import com.myproject.bean.Member;

import java.sql.*;
import java.time.LocalDate;

public class MemberDao extends SuperDao{
    public MemberDao() {
    }

    private Member preMember(ResultSet rs){
        Member member = null;
        try {
            member = new Member();
            member.setId(rs.getString("id"));
            member.setPassword(rs.getString("password"));
            member.setName(rs.getString("name"));
            member.setGender(rs.getString("gender"));
            member.setAge(rs.getInt("age"));
            member.setJoinDate(rs.getDate("join_date").toLocalDate());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return member;
    }

    public int signUp(Member m) {
        int res = 0;

        String sql = "insert into member (id, password, name, gender, age, join_date) values (?, ?, ?, ?, ?, ?)";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, m.getId());
            pstmt.setString(2, m.getPassword());
            pstmt.setString(3, m.getName());
            pstmt.setString(4, m.getGender());
            pstmt.setInt(5, m.getAge());
            pstmt.setDate(6, Date.valueOf(LocalDate.now()));

            res = pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public Member signIn(String id, String pw) {
        Member m = null;

        String sql = "select * from member where id = ? and password = ?";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1, id);
            pstmt.setString(2, pw); // 입력값 대입하고

            try(ResultSet rs = pstmt.executeQuery();) { // 문장 실행해서
                if (rs.next()) { // 존재한다면
                    m = this.preMember(rs); // 저장 (리턴할거)
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return m;
    }

    public Member callById(String loggedInId) {
        Member m = null;

        String sql = "select * from member where id = ?";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1, loggedInId);

            try (ResultSet rs = pstmt.executeQuery();) {
                if (rs.next()) {
                    m = this.preMember(rs);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return m;
    }

    public int updatePw(String loggedInId, String pw) {
        int res = 0;

        String sql1 = "select * from member where id = ? and password = ?"; // 먼저 사용중이던 비번인지 체크하고
        String sql2 = "update member set password = ? where id = ?"; // 아니라면 변경

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt1 = conn.prepareStatement(sql1);) {
            pstmt1.setString(1, loggedInId);
            pstmt1.setString(2, pw);
            ResultSet rs = pstmt1.executeQuery();

            if (!rs.next()) { // 사용중이 아니면 변경하며 res = 1 부여, 없으면 그대로 0
                try (PreparedStatement pstmt2 = conn.prepareStatement(sql2);) {
                    pstmt2.setString(2, loggedInId);
                    pstmt2.setString(1, pw);
                    res = pstmt2.executeUpdate();
                    conn.commit();
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public int deleteOne(String loggedInId, String pw) {
        int res = 0;

        String sql = "delete from member where id = ? and password = ?";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loggedInId);
            pstmt.setString(2, pw);
            res = pstmt.executeUpdate();
            conn.commit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }
}
