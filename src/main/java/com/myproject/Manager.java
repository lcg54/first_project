package com.myproject;

import com.myproject.bean.Member;
import com.myproject.bean.Usage;
import com.myproject.dao.MemberDao;
import com.myproject.dao.UsageDao;

import java.sql.Date;
import java.time.LocalDate;

public class Manager {
    private MemberDao mdao = null;
    private UsageDao udao = null;

    public Manager() {
        this.mdao = new MemberDao();
        this.udao = new UsageDao();
    }

    public boolean checkNewId(String id) {
        Member m = mdao.callById(id);
        if (!id.matches("^[a-zA-Z0-9]{3,6}$")) {
            System.out.println("입력 조건을 확인해주세요.");
            return false;
        } else if (m != null) {
            System.out.println("이미 존재하는 아이디입니다.");
            return false;
        }
        return true;
    }

    public boolean checkId(String id) {
        Member m = mdao.callById(id);
        if (!id.matches("^[a-zA-Z0-9]{3,6}$")) {
            System.out.println("입력 조건을 확인해주세요.");
            return false;
        } else if (m == null) {
            System.out.println("존재하지 않는 아이디입니다.");
            return false;
        }
        return true;
    }

    public boolean checkPw(String pw) {
        if (!pw.matches("^[a-zA-Z0-9]{7,15}$")) {
            System.out.println("입력 조건을 확인해주세요.");
            return false;
        }
        if (!pw.matches(".*[a-zA-Z].*")) {
            System.out.println("비밀번호에 영문이 한 글자 이상 포함되어야 합니다.");
            return false;
        }
        if (!pw.matches(".*[0-9].*")) {
            System.out.println("비밀번호에 숫자가 하나 이상 포함되어야 합니다.");
            return false;
        }
        return true;
    }

    public boolean checkName(String name) {
        if (name.isBlank()) {
            System.out.println("이름은 반드시 입력해야 합니다.");
            return false;
        } else if (name.length() > 10) {
            System.out.println("올바른 이름을 입력해주세요. (1~10자)");
            return false;
        }
        return true;
    }

    public boolean checkGen(String gen) {
        switch (gen) {
            case "1", "m", "M", "male", "Male", "남", "남자", "남성",
                 "2", "f", "F", "female", "Female", "여", "여자", "여성" :
                return true;
            default:
                System.out.println("올바른 성별을 입력해주세요.");
                return false;
        }
    }

    public boolean checkAge(String age) {
        try {
            int a = Integer.parseInt(age);
            if (a < 12) {
                System.out.println("12세 미만은 가입이 불가능합니다.");
                return false;
            } else if (a > 120) {
                System.out.println("올바른 나이를 입력해주세요. (최대 120세)");
                return false;
            } else {
                return true;
            }
        } catch (NumberFormatException e) {
            System.out.println("숫자로 입력해주세요.");
            return false;
        }
    }

    public boolean checkTel(String tel) {
        try {
            int a = Integer.parseInt(tel);
            switch (a) {
                case 1, 2, 3:
                    return true;
                default:
                    System.out.println("지정된 숫자 내에서 입력해주세요.");
                    return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("숫자로 입력해주세요.");
            return false;
        }
    }

    public boolean checkPlan(String plan) {
        try {
            int a = Integer.parseInt(plan);
            switch (a) {
                case 1, 2, 3, 4:
                    return true;
                default:
                    System.out.println("지정된 숫자 내에서 입력해주세요.");
                    return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("숫자로 입력해주세요.");
            return false;
        }
    }



    public boolean signUp(Member m, Usage u) {
        // 입력받지 않은 컬럼의 기본값 set
        m.setJoinDate(LocalDate.now());
        if (mdao.signUp(m) > 0) {
            // 먼저 insert 성공한 member 값을 토대로, usage도 마찬가지로 입력받지 않은 컬럼의 기본값 set
            u.setGrade(udao.calcGrade(m.getJoinDate()));
            u.setDiscountRate(udao.calcDiscount(m.getAge(), u.getGrade()));
            u.setAmount(udao.calcAmount(u.getPlan(), u.getDiscountRate()));
            if (udao.signUp(u) > 0) {
                System.out.println("\'" + m.getId() + "\' 님의 회원가입이 완료되었습니다.");
                System.out.println("다시 로그인해주세요.");
            } else {
                System.out.println("사용정보 등록에 실패하였습니다. 다시 시도해주세요.");
            }
            return true;
        } else {
            System.out.println("회원가입에 실패하였습니다. 다시 시도해주세요.");
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
                System.out.println("요금제 : " + u.getPlan() + " / " + udao.dataOfPlan(u.getPlan()) + "GB" + " / " + udao.amountOfPlan(u.getPlan()) + "원"  );
                System.out.println("데이터 사용량/잔여량 : " + u.getUsedData() + "GB / " + (udao.dataOfPlan(u.getPlan()) - u.getUsedData()) + "GB");
                System.out.println("할인율 : " + u.getDiscountRate() + "%" +
                        " (청소년/노인 할인 : [" + m.getAge() + "]세 - " + udao.calcAgeDiscount(m.getAge()) + "% + " +
                        "멤버십 등급 할인 : [" + u.getGrade() + "] - " + udao.calcGradeDiscount(u.getGrade()) + "%)");
                System.out.println("최종 납부하실 금액 : " + u.getAmount() + "원");
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
        Usage u = udao.callById(loggedInId);
        int res = udao.updatePlan(loggedInId, plan);
        // 요금제 변경했으니 요금도 변경
        u.setAmount(udao.calcAmount(plan, u.getDiscountRate()));
        if (res > 0) {
            System.out.println("요금제를 변경하였습니다.");
            System.out.println("익월부터 " + u.getAmount() + "원의 요금이 부과됩니다.");
            return true;
        } else {
            System.out.println("이미 해당 요금제를 사용중입니다.");
            return false;
        }
    }

    public boolean updateTel(String loggedInId, String tel) {
        int res = udao.updateTel(loggedInId, tel);
        if (res > 0) {
            System.out.println("통신사를 이동하였습니다.");
            return true;
        } else {
            System.out.println("이미 해당 통신사를 사용중입니다.");
            return false;
        }
    }

    public boolean deleteOne(String loggedInId, String outPw) {
        if (mdao.deleteOne(loggedInId, outPw) > 0) {
            udao.deleteOne(loggedInId);
            System.out.println(loggedInId + "님의 회원 탈퇴가 완료되었습니다.");
            System.out.println("로그인 화면으로 이동합니다.");
            return true;
        } else {
            System.out.println("회원 탈퇴에 실패하였습니다. 다시 시도해 주세요.");
            return false;
        }
    }
}
