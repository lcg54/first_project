package com.myproject.bean;

import java.time.LocalDate;

public class Member {
    private String id;
    private String password;
    private String name;
    private String gender;
    private int age;
    private LocalDate joinDate;

    @Override
    public String toString() {
        return name + "(" + age + "/" + gender + ")" + "\n가입일 : " + joinDate;
    }

    public Member(String id, String password, String name, String gender, int age, LocalDate joinDate) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.joinDate = joinDate;
    }

    public Member() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }
}
