package com.gsc.bean;

public class ReportCardRecord {
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
    // 性别
    private String sex;
    // 身份证
    private String idCard;
    // 出生日期
    private String birth;
    // 手机
    private String mobileNo;
    // 专业
    private String profession;
    // 考试阶段
    private String examStage;
    // 试卷规则
    private String examRule;
    //总成绩
    private Double totalScore;
    // 小规则
    private String smallRule;
    // 考试日期
    private String examDate;
    // 考试时间
    private String examTime;
    // 考试类型
    private String examType;
    // 备注
    private String remark;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getExamStage() {
        return examStage;
    }

    public void setExamStage(String examStage) {
        this.examStage = examStage;
    }

    public String getExamRule() {
        return examRule;
    }

    public void setExamRule(String examRule) {
        this.examRule = examRule;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public String getSmallRule() {
        return smallRule;
    }

    public void setSmallRule(String smallRule) {
        this.smallRule = smallRule;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ReportCardRecord{" +
                "num=" + num +
                ", parentDepartment='" + parentDepartment + '\'' +
                ", department='" + department + '\'' +
                ", userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", idCard='" + idCard + '\'' +
                ", birth='" + birth + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", profession='" + profession + '\'' +
                ", examStage='" + examStage + '\'' +
                ", examRule='" + examRule + '\'' +
                ", totalScore=" + totalScore +
                ", smallRule='" + smallRule + '\'' +
                ", examDate='" + examDate + '\'' +
                ", examTime='" + examTime + '\'' +
                ", examType='" + examType + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
