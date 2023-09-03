package com.gsc.bean;

import java.util.ArrayList;
import java.util.List;

public class TeamDataResult {

    //班组名称
    private String department;
    //专业类别
    private String profession;
    //单位名称
    private String company;
    //总分
    private Double answerTotalScore;
    //平均分
    private Double answerAverageScore;
    //总分
    private Double reportCardTotalScore;
    //平均分
    private Double reportCardAverageScore;
    //每日答题参考人数
    private Integer answerAttendNum;
    //普考参考人数
    private Integer reportCardAttendNum;
    //合格人数（总分阈值需要设置 单科成绩阈值也需要设置，三科合格，总分合格才算合格）
    private Integer qualifiedNum;
    //合格率
    private Double qualifiedRate;
    //普考排名（按合格率，分数一样并列）
    private Integer reportCordRank;
    //每日答题排名（按合格率，分数一样并列）
    private Integer answerRank;
    //积分（普考每日答题）
    private Double answerIntegral;
    //积分（普考）
    private Double reportCordIntegral;
    //满分人的名字
    private String fullMarkNames;

    //每日答题记录
    private List<AnswerStatisticsRecord> answerStatisticsRecords;
    //普考答题记录
    private List<ReportCardRecord> reportCardRecords;

    public TeamDataResult(String department) {
        this.department = department;
        this.answerTotalScore = 0.0;
        this.answerAverageScore = 0.0;
        this.reportCardTotalScore = 0.0;
        this.reportCardAverageScore = 0.0;
        this.answerAttendNum = 0;
        this.reportCardAttendNum = 0;
        this.qualifiedNum = 0;
        this.qualifiedRate = 0.0;
        this.answerRank = 0;
        this.reportCordRank = 0;
        this.answerIntegral = 0D;
        this.reportCordIntegral = 0D;
        this.fullMarkNames = "";
        answerStatisticsRecords = new ArrayList<>();
        reportCardRecords = new ArrayList<>();
    }


    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Double getAnswerTotalScore() {
        return answerTotalScore;
    }

    public void setAnswerTotalScore(Double answerTotalScore) {
        this.answerTotalScore = answerTotalScore;
    }

    public Double getAnswerAverageScore() {
        return answerAverageScore;
    }

    public void setAnswerAverageScore(Double answerAverageScore) {
        this.answerAverageScore = answerAverageScore;
    }

    public Double getReportCardTotalScore() {
        return reportCardTotalScore;
    }

    public void setReportCardTotalScore(Double reportCardTotalScore) {
        this.reportCardTotalScore = reportCardTotalScore;
    }

    public Double getReportCardAverageScore() {
        return reportCardAverageScore;
    }

    public void setReportCardAverageScore(Double reportCardAverageScore) {
        this.reportCardAverageScore = reportCardAverageScore;
    }

    public Integer getAnswerAttendNum() {
        return answerAttendNum;
    }

    public void setAnswerAttendNum(Integer answerAttendNum) {
        this.answerAttendNum = answerAttendNum;
    }

    public Integer getReportCardAttendNum() {
        return reportCardAttendNum;
    }

    public void setReportCardAttendNum(Integer reportCardAttendNum) {
        this.reportCardAttendNum = reportCardAttendNum;
    }

    public Integer getQualifiedNum() {
        return qualifiedNum;
    }

    public void setQualifiedNum(Integer qualifiedNum) {
        this.qualifiedNum = qualifiedNum;
    }

    public Double getQualifiedRate() {
        return qualifiedRate;
    }

    public void setQualifiedRate(Double qualifiedRate) {
        this.qualifiedRate = qualifiedRate;
    }

    public Integer getReportCordRank() {
        return reportCordRank;
    }

    public void setReportCordRank(Integer reportCordRank) {
        this.reportCordRank = reportCordRank;
    }

    public Integer getAnswerRank() {
        return answerRank;
    }

    public void setAnswerRank(Integer answerRank) {
        this.answerRank = answerRank;
    }

    public Double getAnswerIntegral() {
        return answerIntegral;
    }

    public void setAnswerIntegral(Double answerIntegral) {
        this.answerIntegral = answerIntegral;
    }

    public Double getReportCordIntegral() {
        return reportCordIntegral;
    }

    public void setReportCordIntegral(Double reportCordIntegral) {
        this.reportCordIntegral = reportCordIntegral;
    }

    public String getFullMarkNames() {
        return fullMarkNames;
    }

    public void setFullMarkNames(String fullMarkNames) {
        this.fullMarkNames = fullMarkNames;
    }

    public List<AnswerStatisticsRecord> getAnswerStatisticsRecords() {
        return answerStatisticsRecords;
    }

    public void setAnswerStatisticsRecords(List<AnswerStatisticsRecord> answerStatisticsRecords) {
        this.answerStatisticsRecords = answerStatisticsRecords;
    }

    public List<ReportCardRecord> getReportCardRecords() {
        return reportCardRecords;
    }

    public void setReportCardRecords(List<ReportCardRecord> reportCardRecords) {
        this.reportCardRecords = reportCardRecords;
    }

    @Override
    public String toString() {
        return "TeamDataResult{" +
                "department='" + department + '\'' +
                ", profession='" + profession + '\'' +
                ", company='" + company + '\'' +
                ", answerTotalScore=" + answerTotalScore +
                ", answerAverageScore=" + answerAverageScore +
                ", reportCardTotalScore=" + reportCardTotalScore +
                ", reportCardAverageScore=" + reportCardAverageScore +
                ", answerAttendNum=" + answerAttendNum +
                ", reportCardAttendNum=" + reportCardAttendNum +
                ", qualifiedNum=" + qualifiedNum +
                ", qualifiedRate=" + qualifiedRate +
                ", reportCordRank=" + reportCordRank +
                ", answerRank=" + answerRank +
                ", answerIntegral=" + answerIntegral +
                ", reportCordIntegral=" + reportCordIntegral +
                ", fullMarkNames='" + fullMarkNames + '\'' +
//                ", answerStatisticsRecords=" + answerStatisticsRecords +
//                ", reportCardRecords=" + reportCardRecords +
                '}';
    }
}
