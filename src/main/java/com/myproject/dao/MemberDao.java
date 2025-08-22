package com.myproject.dao;

import com.myproject.bean.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return member;
    }

    public List<Member> showAll() {
        List<Member> list = new ArrayList<>();

        String sql = "select * from members";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery();){
            while (rs.next()) {
                Member m = this.preMember(rs);
                list.add(m);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public int signUp(Member newM) {
        String sql = "insert into members (id, password, name, gender, age) values (?, ?, ?, ?, ?)";
        int cnt = 0;

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, newM.getId());
            pstmt.setString(2, newM.getPassword());
            pstmt.setString(3, newM.getName());
            pstmt.setString(4, newM.getGender());
            pstmt.setInt(5, newM.getAge());

            cnt = pstmt.executeUpdate();
            //conn.commit();

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) { // primary key(=id)가 중복되는 경우 SQLException 발생
                System.out.println("이미 존재하는 ID입니다.");
            }
            throw new RuntimeException(e);
        }
        return cnt;
    }

    public Member login(String id, String pw) {
        Member m = null;

        String sql = "select * from boards where id = ?, password = ?";

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

    public Member showMyInfo(String loggedInId) {
        Member m = null;

        String sql = "select * from boards where id = ?";

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

        String sql = "update members set password = ? where id = ?";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(2, loggedInId);
            pstmt.setString(1, pw);
            res = pstmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }
}
