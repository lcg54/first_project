package com.myproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

// CREATE USER oraman IDENTIFIED BY oracle DEFAULT TABLESPACE users TEMPORARY TABLESPACE temp;
//GRANT connect, resource TO oraman;
//ALTER USER oraman ACCOUNT UNLOCK;

public class Main {
    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        // JDBC 접속 정보
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String driver = "oracle.jdbc.driver.OracleDriver";
        String id = "sundori" ;
        String password = "hello1234" ;
        try {
            // 1. 드라이버 로드
            Class.forName(driver);

            // 2. 연결
            conn = DriverManager.getConnection(url, id, password);

            // 3. Statement 생성
            stmt = conn.createStatement();

            // 4. 현재 시간 조회
            //String sql = "SELECT 5 + 3 * 4 FROM dual";
            String sql = "SELECT upper('hello') as upp, power(2,3) as pow FROM dual";

            rs = stmt.executeQuery(sql);

            // 5. 결과 출력
            if (rs.next()) {
                //System.out.println("계산 결과 : " + rs.getString(1));
                System.out.println("대문자 : " + rs.getString(1));
                System.out.println("2의 3제곱 : " + rs.getString(2));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 6. 리소스 정리
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }
}