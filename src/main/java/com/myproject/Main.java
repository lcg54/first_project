package com.myproject;

import com.myproject.bean.Member;
import com.myproject.bean.Usage;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Manager manager = new Manager();
        String loggedInId = null;

        System.out.print("프로그램을 시작합니다.");

        // 로그인 루프 : 회원가입 or 로그인
        while (true) {
            try {
                System.out.println("\n로그인 후 이용 가능합니다.");
                System.out.println("0: 종료\t1: 회원가입\t2: 로그인");
                String menu = scan.nextLine();
                switch (menu) {
                    case "0", "종료":
                        System.out.println("프로그램을 종료합니다.");
                        System.exit(0);
                        break;
                    case "1", "회원가입": // 아이디 중복, 입력범위 등 예외처리 다시 짜야함. 각각 메소드 메인에 만드는게 나을듯
                        System.out.println("새 계정을 생성합니다.");
                        Member newM = new Member();
                        Usage newU = new Usage();
                        System.out.print("아이디 (3~6자 이내의 영문, 숫자) : "); newM.setId(scan.nextLine()); newU.setId(scan.nextLine());
                        System.out.print("비밀번호 (7자 이상의 영문, 숫자) : "); newM.setPassword(scan.nextLine());
                        System.out.print("이름 : "); newM.setName(scan.nextLine());
                        System.out.print("성별 (M 또는 F) : "); newM.setGender(scan.nextLine());
                        System.out.print("나이 : "); newM.setAge(Integer.parseInt(scan.nextLine()));
                        System.out.print("통신사 : "); newU.setTelecom(scan.nextLine());
                        System.out.print("요금제 : "); newU.setPlan(scan.nextLine());
                        manager.signUp(newM, newU);
                        break;
                    case "2", "로그인":
                        System.out.print("아이디 : ");String id = scan.nextLine();
                        System.out.print("비밀번호 : ");String pw = scan.nextLine();
                        int a = manager.login(id, pw);
                        if (a == 1) {
                            loggedInId = id;
                        } else {
                            break;
                        }

                        // crud 루프
                        boolean run = true;
                        while (run) {
                            System.out.println("\n원하시는 서비스를 선택해 주세요.");
                            System.out.println("0: 종료\t1: 개인정보 조회\t2: 비밀번호 변경\t3: 요금제 변경\t4: 통신사 변경\t5: 회원탈퇴");
                            try {
                                String mainMenu = scan.nextLine();
                                switch (mainMenu) {
                                    case "0", "종료":
                                        System.out.println("프로그램을 종료합니다. 자동으로 로그아웃됩니다.");
                                        System.exit(0);
                                        break;
                                    case "1", "개인정보 조회":
                                        manager.showMyInfo(loggedInId);
                                        break;
                                    case "2", "비밀번호 변경":
                                        System.out.print("새로 변경할 비밀번호를 입력하세요. (10자 이내의 영문, 숫자) : ");
                                        String newPw = scan.nextLine();
                                        manager.updatePw(loggedInId, newPw);
                                        //재로그인
                                        System.out.print("아이디 : "); String reId = scan.nextLine();
                                        System.out.print("비밀번호 : "); String rePw = scan.nextLine();
                                        int b = manager.login(reId, rePw);
                                        if (b == 0) {run = false;} // 재로그인 실패시 다시 로그인 루프로
                                        loggedInId = reId;
                                        break;
                                    case "3":
                                        boolean stop = false;
                                        while (!stop) {
                                            System.out.println("변경할 요금제를 선택하세요.");
                                            System.out.println("1: 5G 프리미엄\t2: 5G 스탠다드\t3: LTE 무제한\t4: LTE 라이트");
                                            String plan = scan.nextLine();
                                            switch (plan){
                                                case "1","5G 프리미엄" : plan = "5G 프리미엄"; break;
                                                case "2","5G 스탠다드" : plan = "5G 스탠다드"; break;
                                                case "3","LTE 무제한" : plan = "LTE 무제한"; break;
                                                case "4","LTE 라이트" : plan = "LTE 라이트"; break;
                                                default: System.out.println("요금제명을 정확히 입력해주세요."); stop = true;
                                            }
                                            manager.updatePlan(loggedInId, plan);
                                        }
                                        break;
                                    case "4":
                                        boolean stop2 = false;
                                        while (!stop2) {
                                            System.out.println("이동할 통신사를 선택하세요.");
                                            System.out.println("1: SK\t2: KT\t3: LG");
                                            String tel = scan.nextLine();
                                            switch (tel){
                                                case "1","SK" : tel = "SK"; break;
                                                case "2","KT" : tel = "KT"; break;
                                                case "3","LG" : tel = "LG"; break;
                                                default: System.out.println("잘못된 입력입니다."); stop2 = true;
                                            }
                                            manager.updateTelecom(loggedInId, tel);
                                        }
                                        break;
                                    case "5":
                                        System.out.print("계정을 제거합니다. 비밀번호를 다시 입력해 주세요 : "); String outPw = scan.nextLine();
                                        manager.signOut(loggedInId, outPw);
                                        run = false; // 회원 탈퇴 시 다시 로그인 루프로
                                        break;
                                }

                            } catch (NumberFormatException e) {
                                System.out.println("지정된 숫자 내에서 입력해 주세요.");
                            }
                        }
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("지정된 숫자 내에서 입력해 주세요.");
            }
        }

    }
}