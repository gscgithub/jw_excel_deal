package com.gsc.bean;

public class AnswerStatisticsRecord {
    //编号
    private Integer num;
    //父部门
    private String parentDepartment;
    //部门
    private String department;
    //用户名
    private String userName;
    //姓名
    private String name;
    //年龄
    private Integer age;
    //应答题天数
    private Integer shouldAnswerDay;
    //实际答题天数
    private Integer actualAnswerDay;
    //总分
    private Double totalScore;
    //平均分
    private Double average;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(String parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getShouldAnswerDay() {
        return shouldAnswerDay;
    }

    public void setShouldAnswerDay(Integer shouldAnswerDay) {
        this.shouldAnswerDay = shouldAnswerDay;
    }

    public Integer getActualAnswerDay() {
        return actualAnswerDay;
    }

    public void setActualAnswerDay(Integer actualAnswerDay) {
        this.actualAnswerDay = actualAnswerDay;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    @Override
    public String toString() {
        return "AnswerStatisticsRecord{" +
                "num=" + num +
                ", parentDepartment='" + parentDepartment + '\'' +
                ", department='" + department + '\'' +
                ", userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", shouldAnswerDay=" + shouldAnswerDay +
                ", actualAnswerDay=" + actualAnswerDay +
                ", totalScore=" + totalScore +
                ", average=" + average +
                '}';
    }
}
