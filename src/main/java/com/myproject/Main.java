package com.myproject;

import com.myproject.bean.Member;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Manager manager = new Manager();
        String loggedInId = null; // 로그인한 회원 ID (crud에 재사용할거)

        System.out.print("프로그램을 시작합니다.");

        // 회원가입 or 로그인 : 로그인 시 루프 종료
        boolean run = true;
        while (run) {
            try {
                System.out.println("\n원하시는 작업을 선택해 주세요.");
                System.out.println("0:종료, 1:회원가입, 2:로그인");
                int menu = Integer.parseInt(scan.nextLine());
                switch (menu) {
                    case 0:
                        System.out.println("프로그램을 종료합니다.");
                        System.exit(0);
                        break;
                    case 1: // 아이디 중복, 입력범위 등 예외처리 다시 짜야함. 각각 메소드 메인에 만드는게 나을듯
                        System.out.println("새 계정을 생성합니다.");
                        Member newM = new Member();
                        System.out.print("아이디 (6자 이내의 영문, 숫자) : ");newM.setId(scan.nextLine());
                        System.out.print("비밀번호 (10자 이내의 영문, 숫자) : ");newM.setPassword(scan.nextLine());
                        System.out.print("이름 : ");newM.setName(scan.nextLine());
                        System.out.print("성별 (M 또는 F) : ");newM.setGender(scan.nextLine());
                        System.out.print("나이 : ");newM.setAge(Integer.parseInt(scan.nextLine()));
                        manager.signUp(newM);
                        break;
                    case 2:
                        System.out.println("아이디 : ");String id = scan.nextLine();
                        System.out.println("비밀번호 : ");String pw = scan.nextLine();
                        manager.login(id, pw);
                        loggedInId = id;
                        run = false;
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("지정된 숫자 내에서 입력해 주세요.");
            }
        }

        // crud 작업
        while (true) {
            System.out.println("\n원하시는 작업을 선택해 주세요.");
            System.out.println("0:종료, 1:개인정보 조회, 2:비밀번호 변경, 3:요금제 변경, 4: 통신사 변경, 5: 회원탈퇴");
            try {
                int menu = Integer.parseInt(scan.nextLine());
                switch (menu) {
                    case 0:
                        System.out.println("프로그램을 종료합니다.");
                        System.exit(0);
                        break;
                    case 1:
                        manager.showMyInfo(loggedInId);
                        break;
                    case 2:
                        System.out.println("새로 변경할 비밀번호를 입력하세요. (10자 이내의 영문, 숫자) : ");
                        String pw = scan.nextLine();
                        manager.updatePw(loggedInId, pw);
                        //재로그인
                        System.out.println("아이디 : ");String id = scan.nextLine();
                        System.out.println("비밀번호 : ");String newPw = scan.nextLine();
                        manager.login(id, newPw);
                        loggedInId = id;
                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:

                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("지정된 숫자 내에서 입력해 주세요.");
            }
        }

    }
}