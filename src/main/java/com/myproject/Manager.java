package com.myproject;

import com.myproject.bean.Member;
import com.myproject.bean.Usage;
import com.myproject.dao.MemberDao;
import com.myproject.dao.UsageDao;

public class Manager {
    private MemberDao mdao = null;
    private UsageDao udao = null;

    public Manager() {
        this.mdao = new MemberDao();
        this.udao = new UsageDao();
    }

    public boolean checkId(String id) {
        Member m = mdao.callById(id);
        if (!id.matches("^[a-zA-Z0-9]{3,6}$")) {
            System.out.println("입력 조건을 확인해주세요.");
            return true;
        } else if (m == null) {
            System.out.println("존재하지 않는 아이디입니다.");
            return true;
        }
        return false;
    }

    public boolean checkNewId(String id) {
        Member m = mdao.callById(id);
        if (!id.matches("^[a-zA-Z0-9]{3,6}$")) {
            System.out.println("입력 조건을 확인해주세요.");
            return true;
        } else if (m != null) {
            System.out.println("이미 존재하는 아이디입니다.");
            return true;
        }
        return false;
    }

    public boolean checkPw(String pw) {
        if (!pw.matches("^[a-zA-Z0-9]{7,15}$")) {
            System.out.println("입력 조건을 확인해주세요.");
            return true;
        }
        if (!pw.matches(".*[a-zA-Z].*")) {
            System.out.println("비밀번호에 영문이 한 글자 이상 포함되어야 합니다.");
            return true;
        }
        if (!pw.matches(".*[0-9].*")) {
            System.out.println("비밀번호에 숫자가 하나 이상 포함되어야 합니다.");
            return true;
        }
        return false;
    }

    public boolean checkName(String name) {
        if (name.isBlank()) {
            System.out.println("이름은 반드시 입력해야 합니다.");
            return true;
        } else if (name.length() > 10) {
            System.out.println("올바른 이름을 입력해주세요. (1~10자)");
            return true;
        }
        return false;
    }

    public boolean checkGen(String gen) {
        switch (gen) {
            case "1", "m", "M", "male", "Male", "남", "남자", "남성",
                 "2", "f", "F", "female", "Female", "여", "여자", "여성" :
                return false;
            default:
                System.out.println("올바른 성별을 입력해주세요.");
                return true;
        }
    }

    public boolean checkAge(String age) {
        try {
            int a = Integer.parseInt(age);
            if (a < 12) {
                System.out.println("12세 미만은 가입이 불가능합니다.");
                return true;
            } else if (a > 120) {
                System.out.println("올바른 나이를 입력해주세요. (최대 120세)");
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("숫자로 입력해주세요.");
            return true;
        }
    }

    public boolean checkTel(String tel) {
        try {
            int a = Integer.parseInt(tel);
            switch (a) {
                case 1, 2, 3:
                    return false;
                default:
                    System.out.println("지정된 숫자 내에서 입력해주세요.");
                    return true;
            }
        } catch (NumberFormatException e) {
            System.out.println("숫자로 입력해주세요.");
            return true;
        }
    }

    public boolean checkPlan(String plan) {
        try {
            int a = Integer.parseInt(plan);
            switch (a) {
                case 1, 2, 3, 4:
                    return false;
                default:
                    System.out.println("지정된 숫자 내에서 입력해주세요.");
                    return true;
            }
        } catch (NumberFormatException e) {
            System.out.println("숫자로 입력해주세요.");
            return true;
        }
    }



    public boolean signUp(Member m, Usage u) {
        if (mdao.signUp(m) > 0 && udao.signUp(u) > 0) {
            System.out.println("\'" + m.getId() + "\' 님의 회원가입이 완료되었습니다.");
            System.out.println("다시 로그인해주세요.");
            return true;
        } else {
            System.out.println("회원가입에 실패하였습니다. 다시 시도해 주세요.");
            return false;
        }
    }

    public boolean signIn(String id, String pw) {
        Member m = mdao.signIn(id, pw);
        if (m != null){
            System.out.println(m.getName() + "님, 환영합니다!");
            return true;
        } else {
            System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
            return false;
        }
    }

    public void showMyInfo(String loggedInId) {
        Member m = mdao.callById(loggedInId);
        Usage u = udao.callById(loggedInId);
        System.out.println(m);
        System.out.println(u);
        System.out.println();
    }

    public boolean showMyDetailInfo(String yesOrNo, String loggedInId) {
        Member m = mdao.callById(loggedInId);
        Usage u = udao.callById(loggedInId);
        switch (yesOrNo) {
            case "y", "Y":
                System.out.println("요금제명 : " + u.getPlan());
                System.out.println("데이터 제공량 : " + udao.dataOfPlan(loggedInId) + "GB");
                System.out.println("데이터 사용량 : " + u.getUsedData() + "GB");
                System.out.println("잔여 데이터 : " + (udao.dataOfPlan(loggedInId) - u.getUsedData()) + "GB");
                System.out.println("할인율 : " + u.getDiscountRate() + "%");
                System.out.println("청소년/노인 할인 : [" + m.getAge() + "]세 - " + udao.calcAgeDiscount(loggedInId) + "% + " +
                        "멤버십 등급 할인 : [" + udao. calcGrade(loggedInId) + "] - " + udao.calcGradeDiscount(loggedInId) + "%)");
                System.out.println("최종 납부할 금액 : " + udao.calcAmount(loggedInId) + "원");
                break;
            case "n", "N":
                break;
            default:
                System.out.println("Y 또는 N을 입력해주세요.");
                return false;
        }
        return true;
    }

    public boolean updatePw(String loggedInId, String pw) {
        int res = mdao.updatePw(loggedInId, pw);
        if (res > 0) {
            System.out.println("비밀번호를 변경하였습니다.");
            System.out.println("다시 로그인해 주세요. 시작 메뉴로 이동합니다.");
            return true;
        } else {
            System.out.println("이미 사용중이던 비밀번호와 일치합니다.");
            return false;
        }
    }

    public boolean updatePlan(String loggedInId, String plan) {
        int res = udao.updatePlan(loggedInId, plan);
        int amount = udao.calcAmount(loggedInId);
        if (res > 0) {
            System.out.println("요금제를 변경하였습니다.");
            System.out.println("익월부터 " + amount + "원의 요금이 부과됩니다.");
            return true;
        } else {
            System.out.println("이미 해당 요금제를 사용중입니다.");
            return false;
        }
    }

    public boolean updateTel(String loggedInId, String tel) {
        int res = udao.updateTelecom(loggedInId, tel);
        if (res > 0) {
            System.out.println("통신사를 이동하였습니다.");
            return true;
        } else {
            System.out.println("이미 해당 통신사를 사용중입니다.");
            return false;
        }
    }

    public boolean deleteOne(String loggedInId, String outPw) {
        if (mdao.deleteOne(loggedInId, outPw) > 0 && udao.deleteOne(loggedInId) > 0) {
            System.out.println(loggedInId + "님의 회원 탈퇴가 완료되었습니다.");
            System.out.println("로그인 화면으로 이동합니다.");
            return true;
        } else {
            System.out.println("회원 탈퇴에 실패하였습니다. 다시 시도해 주세요.");
            return false;
        }
    }
}
