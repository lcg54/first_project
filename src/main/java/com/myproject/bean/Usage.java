package com.myproject.bean;

public class Usage {
    private String id;
    private String telecom;
    private String plan;
    private double usedData;
    private String grade;
    private int discountRate;
    private int amount;

    @Override
    public String toString() {
        return "가입 PLAN : " + telecom + " / " + plan + "\n멤버십 등급 : " + grade + "\n데이터 사용량 : " + usedData
                + "GB\n이번 달에 지불하실 금액 : " + amount + "원";
    }

    public Usage() {

    }

    public Usage(String id, String telecom, String plan, double usedData, String grade, int discountRate, int amount) {
        this.id = id;
        this.telecom = telecom;
        this.plan = plan;
        this.usedData = usedData;
        this.grade = grade;
        this.discountRate = discountRate;
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

    public double getUsedData() {
        return usedData;
    }

    public void setUsedData(double usedData) {
        this.usedData = usedData;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(int discountRate) {
        this.discountRate = discountRate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
