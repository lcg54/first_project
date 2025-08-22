package com.myproject.bean;

public class Member {
    private String id;
    private String password;
    private String name;
    private String gender;
    private int age;

    @Override
    public String toString() {
        return "\t이름 : " + name +
                "\t성별 : " + gender +
                "\t나이 : " + age;
    }

    public Member(String id, String password, String name, String gender, int age) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.age = age;
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
}
