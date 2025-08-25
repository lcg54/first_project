package com.myproject.bean;

public class Usage {
    private String id;
    private String telecom;
    private String plan;
    private double used_data = 0.00;
    private String grade = "normal";
    private int discount_rate = 0;
    private int amount;

    @Override
    public String toString() {
        return "가입 PLAN : " + telecom + " / " + plan + "\n멤버십 등급 : " + grade + "\n데이터 사용량 : " + used_data
                + "GB\n이번 달에 지불하실 금액 : " + amount + "원";
    }

    public Usage() {

    }

    public Usage(String id, String telecom, String plan, double used_data, String grade, int discount_rate, int amount) {
        this.id = id;
        this.telecom = telecom;
        this.plan = plan;
        this.used_data = used_data;
        this.grade = grade;
        this.discount_rate = discount_rate;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTelecom() {
        return telecom;
    }

    public void setTelecom(String telecom) {
        this.telecom = telecom;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public double getUsed_data() {
        return used_data;
    }

    public void setUsed_data(double used_data) {
        this.used_data = used_data;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(int discount_rate) {
        this.discount_rate = discount_rate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
