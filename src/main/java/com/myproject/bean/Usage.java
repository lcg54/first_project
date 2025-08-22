package com.myproject.bean;

public class Usage {
    private String id;
    private String telecom;
    private String plan;
    private String grade;
    private int discount_rate;
    private int amount;

    @Override
    public String toString() {
        return "통신사 : " + telecom + "\t요금제 : " + plan + "\t등급 : " + grade
                + "\t할인율 : " + discount_rate + "%\t지불하실 금액 : " + amount + "원";
    }

    public Usage() {

    }

    public Usage(String id, String telecom, String plan, String grade, int discount_rate, int amount) {
        this.id = id;
        this.telecom = telecom;
        this.plan = plan;
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
