package com.myproject;

import com.myproject.bean.Member;
import com.myproject.dao.MemberDao;

import java.util.List;

public class Manager {
    private MemberDao mdao = null;

    public Manager() {
        this.mdao = new MemberDao();
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

    public void signUp(Member m) {
        if (mdao.signUp(m) > 0) {
            System.out.println("\'" + m.getId() + "\' 님의 회원가입이 완료되었습니다.");
            System.out.println("다시 로그인해 주세요.");
        } else {
            System.out.println("회원 등록에 실패하였습니다.");
        }
    }

    public void login(String id, String pw) {
        Member m = mdao.login(id, pw);
        if (m != null){
            System.out.println(m.getName() + "님, 환영합니다!");
        } else {
            System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    public void showMyInfo(String loggedInId) {
        Member m = mdao.showMyInfo(loggedInId);
        System.out.println(m); // + usage u
    }

    public void updatePw(String loggedInId, String pw) {
        int res = mdao.updatePw(loggedInId, pw);
        if (res == 1) {
            System.out.println("비밀번호를 변경하였습니다.");
            System.out.println("다시 로그인해 주세요.");
        }
    }
}
