package com.myproject;

import com.myproject.bean.Member;
import com.myproject.bean.Usage;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Manager manager = new Manager();

        System.out.println("프로그램을 시작합니다.");
        // 시작 루프 : 회원가입 or 로그인
        while (true) {
            try {
                System.out.println("\n로그인 후 이용 가능합니다.");
                System.out.println("메뉴를 선택해주세요. (숫자 입력)");
                System.out.println("[0: 종료, 1: 회원가입, 2: 로그인]");
                int startMenu = Integer.parseInt(scan.nextLine());
                switch (startMenu) {
                    case 0:
                        System.out.println("프로그램을 종료합니다.");
                        System.exit(0);
                        break;

                    case 1:
                        System.out.println("새 계정을 생성합니다.");
                        Member m = new Member();
                        Usage u = new Usage();
                        boolean signUp = false;
                        while (!signUp) {
                            // 세터(입력메소드(+유효성검사메소드 내장))
                            m.setId(enterNewId(scan, manager));
                            u.setId(enterNewId(scan, manager));
                            m.setPassword(enterPw(scan, manager));
                            m.setName(enterName(scan, manager));
                            m.setGender(enterGen(scan, manager));
                            m.setAge(Integer.parseInt(enterAge(scan, manager)));
                            u.setTelecom(enterTel(scan, manager));
                            u.setPlan(enterPlan(scan, manager));
                            // 회원가입 성공 시 true 반환 => signUp 루프 탈출
                            signUp = manager.signUp(m, u);
                        }
                        break;

                    case 2:
                        String loggedInId = null;
                        boolean signIn = false;
                        while (!signIn) {
                            String id = enterId(scan, manager);
                            String pw = enterPw(scan, manager);
                            // 로그인 성공 시 signIn 루프 탈출 + 로그인한 아이디 저장
                            signIn = manager.signIn(id, pw);
                            if (signIn) {
                                loggedInId = id;
                            }
                        }
                        // 메인 루프 : 로그인 시 진입
                        boolean main = true;
                        while (main) {
                            System.out.println("메뉴를 선택해주세요. (숫자 입력)");
                            System.out.println("[0: 종료, 1: 내 정보, 2: 비밀번호 변경, 3: 요금제 변경, 4: 통신사 이동, 5: 회원탈퇴, 6: 로그아웃]");
                            try {
                                boolean act;
                                int mainMenu = Integer.parseInt(scan.nextLine());
                                switch (mainMenu) {
                                    case 0:
                                        System.out.println("프로그램을 종료합니다. 자동으로 로그아웃됩니다.");
                                        System.exit(0);
                                        break;

                                    case 1:
                                        System.out.println("회원 정보를 출력합니다.");
                                        manager.showMyInfo(loggedInId);
                                        act = false;
                                        while (!act) {
                                            System.out.println("세부 명세서를 조회하시겠습니까? (Y/N)");
                                            String yesOrNo = scan.nextLine();
                                            // Y/N 이외의 값 입력 시 다시 묻기
                                            act = manager.showMyDetailInfo(yesOrNo, loggedInId);
                                        }
                                        break;

                                    case 2:
                                        act = true;
                                        while (act) {
                                            System.out.print("변경할 ");
                                            String newPw = enterPw(scan, manager);
                                            // 비밀번호 변경 성공 시 시작 루프로 이동(재로그인)
                                            if (manager.updatePw(loggedInId, newPw)) {
                                                main = false;
                                                act = false;
                                            }
                                        }
                                        break;

                                    case 3:
                                        act = true;
                                        while (act) {
                                            System.out.print("변경할 ");
                                            String newPlan = enterPlan(scan, manager);
                                            // 요금제 변경 성공 시 메인 루프로
                                            if (manager.updatePlan(loggedInId, newPlan)) {
                                                act = false;
                                            }
                                        }
                                        break;

                                    case 4:
                                        act = true;
                                        while (act) {
                                            System.out.print("이동할 ");
                                            String newTel = enterTel(scan, manager);
                                            // 통신사 이동 성공 시 메인 루프로
                                            if (manager.updateTel(loggedInId, newTel)) {
                                                act = false;
                                            }
                                        }
                                        break;

                                    case 5:
                                        System.out.println("계정을 제거합니다.");
                                        act = true;
                                        while (act) {
                                            System.out.print("확인을 위해 다시 한 번 ");
                                            String rePw = enterPw(scan, manager);
                                            // 탈퇴 후 시작 루프로
                                            if (manager.deleteOne(loggedInId, rePw)) {
                                                main = false;
                                                act = false;
                                            }
                                        }
                                        break;

                                    case 6:
                                        act = false;
                                        while (!act) {
                                            System.out.println("로그아웃 하시겠습니까? (Y/N)");
                                            String yesOrNo = scan.nextLine();
                                            // Y/N 이외의 값 입력 시 다시 묻기
                                            switch (yesOrNo) {
                                                case "y", "Y":
                                                    System.out.println("로그아웃 되었습니다.");
                                                    act = true;
                                                    break;
                                                case "n", "N":
                                                    act = true;
                                                    break;
                                                default:
                                                    System.out.println("Y 또는 N을 입력해주세요.");
                                            }
                                        }

                                    default:
                                        System.out.println("지정된 숫자 내에서 입력해주세요.");
                                }

                            } catch (NumberFormatException e) {
                                System.out.println("숫자로 입력해주세요.");
                            }
                        }
                        break;

                    default:
                        System.out.println("지정된 숫자 내에서 입력해주세요.");
                }

            } catch (NumberFormatException e) {
                System.out.println("숫자로 입력해주세요.");
            }
        }

    }

    private static String enterNewId(Scanner scan, Manager manager) {
        String id = null;
        boolean checkAndConfirm = true;
        while (checkAndConfirm) {
            // 유효성검사(+중복여부)
            boolean check = true;
            while (check) {
                System.out.println("아이디를 입력해주세요. (3~6자 이내의 영문, 숫자)");
                id = scan.nextLine();
                // 검사 통과시 false 반환 (check 루프 탈출)
                check = manager.checkNewId(id);
            }
            // id 확정(컨펌)여부
            System.out.println("사용 가능한 아이디입니다.");
            System.out.print("아이디는 최초 설정 후 변경이 불가능합니다.");
            boolean confirm = true;
            while (confirm) {
                System.out.println("정말 " + id + "(으)로 결정하시겠습니까? (Y/N)");
                String yesOrNo = scan.nextLine();
                switch (yesOrNo) {
                    case "y", "Y": // 루프 모두 탈출하고 return id
                        checkAndConfirm = false;
                        confirm = false;
                        break;
                    case "n", "N": // 다시 유효성검사로
                        confirm = false;
                        break;
                    default: // 다시 컨펌으로
                        System.out.println("Y 또는 N을 입력해주세요.");
                }
            }
        }
        return id;
    }

    private static String enterId(Scanner scan, Manager manager) {
        String id = null;
        boolean check = true;
        while (check) {
            System.out.println("아이디를 입력해주세요. (3~6자 이내의 영문, 숫자)");
            id = scan.nextLine();
            check = manager.checkId(id);
        }
        return id;
    }

    private static String enterPw(Scanner scan, Manager manager) {
        String pw = null;
        boolean check = true;
        while (check) {
            System.out.println("비밀번호를 입력해주세요. (7~15자 이내의 영문, 숫자)");
            pw = scan.nextLine();
            check = manager.checkPw(pw);
        }
        return pw;
    }

    private static String enterName(Scanner scan, Manager manager) {
        String name = null;
        boolean check = true;
        while (check) {
            System.out.println("이름를 입력해주세요.");
            name = scan.nextLine();
            check = manager.checkName(name);
        }
        return name;
    }

    private static String enterGen(Scanner scan, Manager manager) {
        String gen = null;
        boolean check = true;
        while (check) {
            System.out.println("성별을 입력해주세요.");
            gen = scan.nextLine();
            check = manager.checkGen(gen);
        }
        switch (gen) {
            case "1", "m", "M", "male", "Male", "남", "남자", "남성": gen = "M"; break;
            case "2", "f", "F", "female", "Female", "여", "여자", "여성": gen = "F"; break;
        }
        return gen;
    }

    private static String enterAge(Scanner scan, Manager manager) {
        String age = null;
        boolean check = true;
        while (check) {
            System.out.println("나이를 입력해주세요.");
            age = scan.nextLine();
            check = manager.checkAge(age);
        }
        return age;
    }

    private static String enterTel(Scanner scan, Manager manager) {
        String tel = null;
        boolean check = true;
        while (check) {
            System.out.println("가입할 통신사를 선택해주세요. (숫자 입력)");
            System.out.print("[1: SK, 2: KT, 3: LG]");
            tel = scan.nextLine();
            check = manager.checkTel(tel);
        }
        switch (tel) {
            case "1": tel = "SK"; break;
            case "2": tel = "KT"; break;
            case "3": tel = "LG"; break;
        }
        return tel;
    }

    private static String enterPlan(Scanner scan, Manager manager) {
        String plan = null;
        boolean check = true;
        while (check) {
            System.out.println("요금제를 선택해주세요. (숫자 입력)");
            System.out.println("[1: 5G 프리미엄, 2: 5G 스탠다드, 3: LTE 무제한, 4: LTE 라이트]");
            plan = scan.nextLine();
            check = manager.checkPlan(plan);
        }
        switch (plan) {
            case "1": plan = "5G 프리미엄"; break;
            case "2": plan = "5G 스탠다드"; break;
            case "3": plan = "LTE 무제한"; break;
            case "4": plan = "LTE 라이트"; break;
        }
        return plan;
    }
}