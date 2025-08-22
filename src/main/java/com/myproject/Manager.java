package com.myproject;

import com.myproject.bean.Member;
import com.myproject.bean.Usage;
import com.myproject.dao.MemberDao;
import com.myproject.dao.UsageDao;

import java.util.List;

public class Manager {
    private MemberDao mdao = null;
    private UsageDao udao = null;

    public Manager() {
        this.mdao = new MemberDao();
        this.udao = new UsageDao();
    }

    public void showAll() {
        List<Member> list = mdao.showAll();
        if (!list.isEmpty()){
            for (Member m:list){
                String id = m.getId();
                //String password = m.getPassword();
                String name = m.getName();
                String gender = m.getGender();
                int age = m.getAge();
                System.out.println("ID : " + id + "\t비밀번호 : *******" + "\t이름 : " + name + "\t성별 : " + gender + "\t나이 : " + age);
            }
        }else{
            System.out.println("회원이 존재하지 않습니다.");
        }
    }

    public void signUp(Member m, Usage u) {
        if (mdao.signUp(m) > 0 && udao.signUp(u) > 0) {
            System.out.println("\'" + m.getId() + "\' 님의 회원가입이 완료되었습니다.");
            System.out.println("다시 로그인해 주세요.");
        } else {
            System.out.println("회원가입에 실패하였습니다. 입력값을 확인해 주세요.");
        }
    }

    public int login(String id, String pw) {
        int a = 0;
        Member m = mdao.login(id, pw);
        if (m != null){
            System.out.println(m.getName() + "님, 환영합니다!");
            a = 1;
        } else {
            System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        return a;
    }

    public void showMyInfo(String loggedInId) {
        Member m = mdao.showMyInfo(loggedInId);
        Usage u = udao.showMyInfo(loggedInId);
        System.out.print(m + "\t");
        System.out.println(u);
    }

    public void updatePw(String loggedInId, String pw) {
        int res = mdao.updatePw(loggedInId, pw);
        if (res > 0) {
            System.out.println("비밀번호를 변경하였습니다.");
            System.out.println("다시 로그인해 주세요.");
        } else {
            System.out.println("이미 사용중이던 비밀번호와 일치합니다.");
        }
    }

    public void updatePlan(String loggedInId, String plan) {
        int res = udao.updatePlan(loggedInId, plan);
        int amount = udao.calcAmount(plan);
        if (res > 0) {
            System.out.println("요금제를 변경하였습니다.");
            System.out.println("익월부터 " + amount + "원의 요금이 부과됩니다.");
        } else {
            System.out.println("이미 해당 요금제를 사용중입니다.");
        }
    }


    public void updateTelecom(String loggedInId, String tel) {
        int res = udao.updateTelecom(loggedInId, tel);
        if (res > 0) {
            System.out.println("통신사를 이동하였습니다.");
        } else {
            System.out.println("이미 해당 통신사를 사용중입니다.");
        }
    }


    public void signOut(String loggedInId, String outPw) {
        if (mdao.signOut(loggedInId, outPw) > 0) {
            udao.signOut(loggedInId);
            System.out.println(loggedInId + "님의 회원 탈퇴가 완료되었습니다.");
            System.out.println("로그인 화면으로 이동합니다.");
        } else {
            System.out.println("회원가입에 실패하였습니다. 입력값을 확인해 주세요.");
        }
    }
}
