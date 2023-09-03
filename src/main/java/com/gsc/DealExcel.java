package com.gsc;

import com.gsc.bean.AnswerStatisticsRecord;
import com.gsc.bean.ReportCardRecord;
import com.gsc.bean.TeamDataResult;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class DealExcel {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("输入每日答题文件地址：");
        String answerStatisticsFile = sc.nextLine();
//        String answerStatisticsFile = "F:\\..学习资料\\自己做的\\佳旺\\统计考试积分2\\每日答题统计_04.25-05.25.xls";
        System.out.println("输入成绩单文件地址：");
        String reportCardFile = sc.nextLine();
//        String reportCardFile = "F:\\..学习资料\\自己做的\\佳旺\\统计考试积分2\\成绩单试验.xls";
        System.out.println("输入附件文件地址：");
        String appendixFile = sc.nextLine();
//        String appendixFile = "F:\\..学习资料\\自己做的\\佳旺\\统计考试积分2\\附件3：竞赛活动内容统计表(5月).xls";

        Workbook answerStatisticsWb = readExcel(answerStatisticsFile);
        Workbook reportCardWb = readExcel(reportCardFile);
        Workbook appendixWb = readExcel(appendixFile);
        Map<String, TeamDataResult> teamDataResults = getDateFromWorkbook(answerStatisticsWb, reportCardWb, appendixWb);
        //计算每日答题积分
        calcAnswerIntegralAndSort(teamDataResults);
        //计算普考积分
        rankByTotalScoreAndCalcIntegral(teamDataResults);
        dealAppendixWb(teamDataResults, appendixWb);
        writeWbToFile(appendixWb, appendixFile);

    }

    private static void calcAnswerIntegralAndSort(Map<String, TeamDataResult> teamDataResults) {

        Map<String, List<TeamDataResult>> professionMap = new HashMap<String, List<TeamDataResult>>();
        for (TeamDataResult teamDataResult : teamDataResults.values()) {
            if(teamDataResult.getProfession() == null) {
                continue;
            }
            List<TeamDataResult> teamDataResultsForProfession = professionMap.get(teamDataResult.getProfession());
            if(teamDataResultsForProfession == null) {
                teamDataResultsForProfession = new ArrayList<>();
                professionMap.put(teamDataResult.getProfession(), teamDataResultsForProfession);
            }
            teamDataResultsForProfession.add(teamDataResult);
        }

        //计算每日答题积分
        for (String profession : professionMap.keySet()) {
            List<TeamDataResult> teams = professionMap.get(profession);
            Collections.sort(teams, (team1, team2) -> {
                return -Double.compare(team1.getAnswerAverageScore(), team2.getAnswerAverageScore());
            });
            Double prizeSocre = 1.25;
            TeamDataResult teamDataResult = teams.get(0);
            teamDataResult.setAnswerIntegral(prizeSocre);
            for (int i = 1; i < teams.size(); i++) {
                if(Double.compare(teams.get(i-1).getAnswerAverageScore(), teams.get(i).getAnswerAverageScore()) != 0) {
                    prizeSocre = prizeSocre - 0.125;
                }
                if(Double.compare(prizeSocre, 0) <= 0) {
                    break;
                }
                teams.get(i).setAnswerIntegral(prizeSocre);

            }
        }

        //计算每日答题排名
        for (String profession : professionMap.keySet()) {
            List<TeamDataResult> teams = professionMap.get(profession);
            int rank = 1;
            teams.get(0).setAnswerRank(rank);
            for (int i = 1; i < teams.size(); i++) {
                if(0 == Double.compare(teams.get(i).getAnswerAverageScore(), teams.get(i-1).getAnswerAverageScore())) {
                    teams.get(i).setAnswerRank(rank);
                } else {
                    rank++;
                    teams.get(i).setAnswerRank(rank);
                }
            }
        }

    }

    private static void writeWbToFile(Workbook resultWb, String filePath) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            resultWb.write(out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void dealAppendixWb(Map<String, TeamDataResult> teamDataResults, Workbook appendixWb) {

        if(appendixWb == null){
            throw new RuntimeException("appendixWb is null");
        }
        Sheet sheet = null;
        org.apache.poi.ss.usermodel.Row row = null;

        //获取第一个sheet
        sheet = appendixWb.getSheetAt(0);
        //获取最大行数
        int rownum = sheet.getPhysicalNumberOfRows();

        for (int i = 3; i < rownum; i++) {
            row = sheet.getRow(i);
            if(row !=null){
                //普考：实考人数 5	 合格人数 6     合格率 7    普考积分 8
                String department = ((String) getCellFormatValue(row.getCell(3)));
                String parentDepartment = ((String) getCellFormatValue(row.getCell(2)));
                department = department.toUpperCase();
                parentDepartment = parentDepartment.toUpperCase();
                TeamDataResult teamDataResult = teamDataResults.get(parentDepartment + department);
                if(teamDataResult == null) {
                    teamDataResult = new TeamDataResult(department);
                }
                if(i >= rownum-1) {
                    break;
                }
                Cell cell4 = row.getCell(4);
                cell4.setCellValue(teamDataResult.getReportCardAttendNum());
                Cell cell5 = row.getCell(5);
                cell5.setCellValue(teamDataResult.getQualifiedNum());
                Cell cell6 = row.getCell(6);
                cell6.setCellValue(toTwoDecimalPlaces(teamDataResult.getQualifiedRate() * 100));
                Cell cell8 = row.getCell(7);
                cell8.setCellValue(toOneDecimalPlaces(teamDataResult.getReportCordIntegral()));

                Cell cell16 = row.getCell(8);
                cell16.setCellValue(teamDataResult.getAnswerTotalScore());
                Cell cell17 = row.getCell(9);
                cell17.setCellValue(teamDataResult.getAnswerAverageScore());
                Cell cell18 = row.getCell(10);
                cell18.setCellValue(toThreeDecimalPlaces(teamDataResult.getAnswerIntegral()));

            }
        }

    }

    private static String toOneDecimalPlaces(double decimal) {
        BigDecimal b = new BigDecimal(decimal);
        return b.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
    }

    private static String toTwoDecimalPlaces(double decimal) {
        BigDecimal b = new BigDecimal(decimal);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    private static String toThreeDecimalPlaces(double decimal) {
        BigDecimal b = new BigDecimal(decimal);
        return b.setScale(3, BigDecimal.ROUND_HALF_UP).toString();
    }

        private static Map<String, TeamDataResult> getDateFromWorkbook(Workbook answerStatisticsWb,Workbook reportCardWb,Workbook appendixWb) {
        Map<String, TeamDataResult> teamDataResults = getDateFromAnswerStatisticsWb(answerStatisticsWb);
        getDateFromReportCardWb(teamDataResults, reportCardWb);
        setProfessionFromAppendixWb(teamDataResults, appendixWb);
        return teamDataResults;
    }

    private static void setProfessionFromAppendixWb(Map<String, TeamDataResult> teamDataResults, Workbook appendixWb) {
        String columns[] = {"序号","专业类别","单位名称","班组名称"};
        List<Map<String,String>> list = getListMapFromWorkbook(appendixWb,  columns);
        for (Map<String, String> appendixMap : list) {
            TeamDataResult teamDataResult = teamDataResults.get(appendixMap.get("单位名称") + appendixMap.get("班组名称"));
            if(teamDataResult != null) {
                teamDataResult.setProfession(appendixMap.get("专业类别"));
            }
        }
    }

    private static void getDateFromReportCardWb(Map<String, TeamDataResult> teamDataResults, Workbook reportCardWb) {
        String columns[] = {"编号","父部门","部门","用户名","姓名","性别","身份证","出生日期","手机","专业","考试阶段","试卷规则",
                "总成绩","小规则","考试日期","考试时间","考试类型","备注"};
        List<Map<String,String>> list = getListMapFromWorkbook(reportCardWb, columns);
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> reportCardMap = list.get(i);
            String num = MapUtils.getString(reportCardMap, "编号");
            if(StringUtils.isNotBlank(num) && StringUtils.isNumeric(num)) {
                String smallRule1 = list.get(i).get("小规则");
                String smallRule2 = list.get(i+1).get("小规则");
                String smallRule3 = list.get(i+2).get("小规则");
                reportCardMap.put("小规则", smallRule1 + "&" + smallRule2 + "&" + smallRule3);
                ++i;
                ++i;
            }
        }
        for (Iterator<Map<String, String>> iterator = list.iterator(); iterator.hasNext(); ) {
            Map<String, String> next = iterator.next();
            if(!StringUtils.isNumeric(MapUtils.getString(next, "编号")) || StringUtils.isEmpty(MapUtils.getString(next, "编号"))) {
                iterator.remove();
            }
        }
        List<ReportCardRecord> reportCardRecords = convertReportCardRecord(list);
        dealReportCardRecord(teamDataResults, reportCardRecords);
    }

    private static void dealReportCardRecord(Map<String, TeamDataResult> teamDataResultMaps, List<ReportCardRecord> reportCardRecords) {

        for(ReportCardRecord reportCardRecord : reportCardRecords) {
            String department = reportCardRecord.getDepartment();
            String parentDepartment = reportCardRecord.getParentDepartment();
            if(parentDepartment.contains("/")) {
                parentDepartment = parentDepartment.split("/")[1];
            }
            TeamDataResult teamDataResult = teamDataResultMaps.get(parentDepartment+department);
            if(teamDataResult != null) {
                teamDataResult.getReportCardRecords().add(reportCardRecord);
            }
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("输入单科成绩合格阈值：");
        String singleThresholdStr = sc.nextLine();
        Double singleThreshold = Double.valueOf(singleThresholdStr);
        System.out.println("输入总成绩合格阈值：");
        String totalThresholdStr = sc.nextLine();
        Double totalThreshold  = Double.valueOf(totalThresholdStr);

        for(TeamDataResult teamDataResult : teamDataResultMaps.values()) {
            //设置普考参考人数
            teamDataResult.setReportCardAttendNum(teamDataResult.getReportCardRecords().size());
            calcQualifiedNum(teamDataResult, singleThreshold, totalThreshold);
            calcAverageScore(teamDataResult);
        }
        List<TeamDataResult> teamDataResults = new ArrayList<>();
        teamDataResults.addAll(teamDataResultMaps.values());
//        rankByTotalScoreAndCalcIntegral(teamDataResults);

    }

    private static void calcAverageScore(TeamDataResult teamDataResult) {
        List<ReportCardRecord> reportCardRecords = teamDataResult.getReportCardRecords();
        Double totalScore = 0.0;
        Double averageScore = 0.0;
        for (ReportCardRecord reportCardRecord : reportCardRecords) {
            totalScore = totalScore + reportCardRecord.getTotalScore();
        }

        if(reportCardRecords.size() != 0) {
            averageScore =  totalScore/reportCardRecords.size();
        }
        teamDataResult.setReportCardTotalScore(totalScore);
        teamDataResult.setReportCardAverageScore(averageScore);
    }

    private static void rankByTotalScoreAndCalcIntegral(Map<String, TeamDataResult> teamDataResults) {
        Map<String, List<TeamDataResult>> professionMap = new HashMap<String, List<TeamDataResult>>();
        for (TeamDataResult teamDataResult : teamDataResults.values()) {
            if(teamDataResult.getProfession() == null) {
                continue;
            }
            List<TeamDataResult> teamDataResultsForProfession = professionMap.get(teamDataResult.getProfession());
            if(teamDataResultsForProfession == null) {
                teamDataResultsForProfession = new ArrayList<>();
                professionMap.put(teamDataResult.getProfession(), teamDataResultsForProfession);
            }
            teamDataResultsForProfession.add(teamDataResult);
        }

        for (List<TeamDataResult> t : professionMap.values()) {
            rankByTotalScoreAndCalcIntegral(t);
        }
    }

    private static void rankByTotalScoreAndCalcIntegral(List<TeamDataResult> teamDataResults) {
        Collections.sort(teamDataResults, (t1, t2) -> {
            return Double.compare(t2.getReportCardAverageScore(), t1.getReportCardAverageScore());
        });
        int rank = 1;
        teamDataResults.get(0).setReportCordRank(rank);
        for (int i = 1; i < teamDataResults.size(); i++) {
            if(0 == Double.compare(teamDataResults.get(i).getReportCardAverageScore(), teamDataResults.get(i-1).getReportCardAverageScore())) {
                teamDataResults.get(i).setReportCordRank(rank);
            } else {
                rank++;
                teamDataResults.get(i).setReportCordRank(rank);
            }
        }

        //计算普考积分
        Double prizeSocre = 3.0;
        TeamDataResult teamDataResult = teamDataResults.get(0);
        if (Double.compare(1, teamDataResult.getQualifiedRate()) == 0) {
            teamDataResult.setReportCordIntegral(prizeSocre);
        } else {
            return;
        }
        for (int i = 1; i < teamDataResults.size() && teamDataResult.getReportCordRank() < 15; i++) {
            if(Double.compare(teamDataResults.get(i).getQualifiedRate(), 1) < 0) {
                break;
            }
            if(Double.compare(teamDataResults.get(i).getReportCardAverageScore(), teamDataResults.get(i-1).getReportCardAverageScore()) != 0) {
                prizeSocre = prizeSocre - 0.2;
            }

            if(Double.compare(prizeSocre, 0) <= 0) {
                break;
            }
            teamDataResults.get(i).setReportCordIntegral(prizeSocre);
        }

        //合格率100的加3分基础分
        for (int i = 0; i < teamDataResults.size() ; i++) {
            if(Double.compare(teamDataResults.get(i).getQualifiedRate(), 1) >= 0) {
                Double reportCordIntegral = teamDataResults.get(i).getReportCordIntegral();
                teamDataResults.get(i).setReportCordIntegral(reportCordIntegral + 3.0);
            }
        }
    }

    private static void calcQualifiedNum(TeamDataResult teamDataResult, Double singleThreshold, Double totalThreshold) {

        List<ReportCardRecord> reportCardRecords = teamDataResult.getReportCardRecords();
        for (ReportCardRecord reportCardRecord : reportCardRecords) {
            //第一部分 安规部分-变电,98分&第二部分 专业理论部分,93分&第三部分 专业实操部分,95分
            String smallRuleStr = reportCardRecord.getSmallRule();
            String[] smallRules = smallRuleStr.split("&");
            String score0 = smallRules[0].substring(smallRules[0].indexOf(",") + 1, smallRules[0].lastIndexOf("分"));
            String score1 = smallRules[1].substring(smallRules[1].indexOf(",") + 1, smallRules[1].lastIndexOf("分"));
            String score2 = smallRules[2].substring(smallRules[2].indexOf(",") + 1, smallRules[2].lastIndexOf("分"));
            double s0 = Double.parseDouble(score0);
            double s1 = Double.parseDouble(score1);
            double s2 = Double.parseDouble(score2);
            double totalScore = s0 + s1 + s2;
            if(Double.compare(s0, singleThreshold) >= 0 && Double.compare(s1, singleThreshold) >= 0
                    && Double.compare(s2, singleThreshold) >= 0 && Double.compare(totalScore, totalThreshold) >= 0) {
                teamDataResult.setQualifiedNum(teamDataResult.getQualifiedNum()+1);
            }
            if(Double.compare(totalScore,300) >= 0) {
                String fullMarkNames = teamDataResult.getFullMarkNames();
                if(StringUtils.isNotEmpty(fullMarkNames)) {
                    fullMarkNames = fullMarkNames + "," + reportCardRecord.getName();
                } else {
                    fullMarkNames = reportCardRecord.getName();
                }
                teamDataResult.setFullMarkNames(fullMarkNames);
            }
        }
        Double qualifiedRate = 0D;
        if(teamDataResult.getReportCardAttendNum() != 0) {
            qualifiedRate =  ((double) (teamDataResult.getQualifiedNum()))/teamDataResult.getReportCardAttendNum();
        }
        teamDataResult.setQualifiedRate(qualifiedRate);
    }

    private static List<ReportCardRecord> convertReportCardRecord(List<Map<String, String>> list) {
        List<ReportCardRecord> reportCardRecords = new ArrayList<>();
        for (Map<String, String> reportCardRecordMap : list) {
            ReportCardRecord reportCardRecord = new ReportCardRecord();
            reportCardRecord.setNum(MapUtils.getInteger(reportCardRecordMap, "编号"));
            reportCardRecord.setParentDepartment(MapUtils.getString(reportCardRecordMap, "父部门"));
            reportCardRecord.setDepartment(MapUtils.getString(reportCardRecordMap, "部门").toUpperCase());
            reportCardRecord.setUserName(MapUtils.getString(reportCardRecordMap, "用户名"));
            reportCardRecord.setName(MapUtils.getString(reportCardRecordMap, "姓名"));
            reportCardRecord.setSex(MapUtils.getString(reportCardRecordMap, "性别"));
            reportCardRecord.setIdCard(MapUtils.getString(reportCardRecordMap, "身份证"));
            reportCardRecord.setBirth(MapUtils.getString(reportCardRecordMap, "出生日期"));
            reportCardRecord.setMobileNo(MapUtils.getString(reportCardRecordMap, "手机"));
            reportCardRecord.setProfession(MapUtils.getString(reportCardRecordMap, "专业"));
            reportCardRecord.setExamStage(MapUtils.getString(reportCardRecordMap, "考试阶段"));
            reportCardRecord.setExamRule(MapUtils.getString(reportCardRecordMap, "试卷规则"));
            reportCardRecord.setTotalScore(MapUtils.getDouble(reportCardRecordMap, "总成绩"));
            reportCardRecord.setSmallRule(MapUtils.getString(reportCardRecordMap, "小规则"));
            reportCardRecord.setExamDate(MapUtils.getString(reportCardRecordMap, "考试日期"));
            reportCardRecord.setExamTime(MapUtils.getString(reportCardRecordMap, "考试时间"));
            reportCardRecord.setExamType(MapUtils.getString(reportCardRecordMap, "考试类型"));
            reportCardRecord.setRemark(MapUtils.getString(reportCardRecordMap, "备注"));
            reportCardRecords.add(reportCardRecord);
        }
        return reportCardRecords;
    }

    private static Map<String, TeamDataResult> getDateFromAnswerStatisticsWb(Workbook answerStatisticsWb) {
        Map<String, TeamDataResult> teamDataResults = new HashMap<>();
        String columns[] = {"编号","父部门","部门","用户名","姓名","年龄","应答题天数","实际答题天数","总分","平均分"};
        List<Map<String,String>> answerStatisticsList = getListMapFromWorkbook(answerStatisticsWb, columns);

        //删除无效数据
        for (Iterator<Map<String, String>> iterator = answerStatisticsList.iterator(); iterator.hasNext(); ) {
            Map<String, String> next = iterator.next();
            if(!StringUtils.isNumeric(MapUtils.getString(next, "编号")) || StringUtils.isEmpty(MapUtils.getString(next, "编号"))) {
                iterator.remove();
            }
        }

        for (Map<String,String> answerStatisticsMap : answerStatisticsList) {
            AnswerStatisticsRecord answerStatisticsRecord = convertAnswerStatisticsRecord(answerStatisticsMap);
            String department = answerStatisticsRecord.getDepartment();
            String parentDepartment = answerStatisticsRecord.getParentDepartment();
            if(parentDepartment.contains("/")) {
                parentDepartment = parentDepartment.split("/")[1];
            }
            TeamDataResult teamDataResult = teamDataResults.get(parentDepartment + department);
            if(teamDataResult == null) {
                teamDataResult = new TeamDataResult(department);
            }
            teamDataResult.getAnswerStatisticsRecords().add(answerStatisticsRecord);
            teamDataResults.put(parentDepartment + department , teamDataResult);
        }
        calcAnswerStatisticsDate(teamDataResults);
        List<TeamDataResult> ts = new ArrayList<>();
        ts.addAll(teamDataResults.values());
        return teamDataResults;
    }

    private static void calcAnswerStatisticsDate(Map<String, TeamDataResult> teamDataResults) {
        for (TeamDataResult teamDataResult : teamDataResults.values()) {
            //平均分（总分/天数/人数）
            List<AnswerStatisticsRecord> answerStatisticsRecords = teamDataResult.getAnswerStatisticsRecords();
            BigDecimal totalScore = BigDecimal.ZERO;
            BigDecimal totalAverageScore = BigDecimal.ZERO;
            for (AnswerStatisticsRecord answerStatisticsRecord : answerStatisticsRecords) {
                totalScore = totalScore.add(BigDecimal.valueOf(answerStatisticsRecord.getTotalScore()));
                totalAverageScore = totalAverageScore.add(BigDecimal.valueOf(answerStatisticsRecord.getAverage()));
            }
            BigDecimal averageScore = totalAverageScore.divide(BigDecimal.valueOf(answerStatisticsRecords.size()), RoundingMode.HALF_DOWN);
            teamDataResult.setAnswerTotalScore(Double.valueOf(totalScore.toString()));
            teamDataResult.setAnswerAverageScore(Double.valueOf(averageScore.toString()));

            //每日答题参考人数
            teamDataResult.setAnswerAttendNum(answerStatisticsRecords.size());

        }
    }

    private static AnswerStatisticsRecord convertAnswerStatisticsRecord(Map<String,String> map) {
        AnswerStatisticsRecord answerStatisticsRecord = new AnswerStatisticsRecord();
        answerStatisticsRecord.setNum(MapUtils.getInteger(map, "编号"));
        answerStatisticsRecord.setParentDepartment(MapUtils.getString(map, "父部门"));
        answerStatisticsRecord.setDepartment(MapUtils.getString(map, "部门").toUpperCase());
        answerStatisticsRecord.setUserName(MapUtils.getString(map, "用户名"));
        answerStatisticsRecord.setName(MapUtils.getString(map, "姓名"));
        answerStatisticsRecord.setAge(MapUtils.getInteger(map, "年龄"));
        answerStatisticsRecord.setShouldAnswerDay(MapUtils.getInteger(map, "应答题天数"));
        answerStatisticsRecord.setActualAnswerDay(MapUtils.getInteger(map, "实际答题天数", 0));
        answerStatisticsRecord.setTotalScore(MapUtils.getDouble(map, "总分", 0.0));
        answerStatisticsRecord.setAverage(MapUtils.getDouble(map, "平均分", 0.0));
        return answerStatisticsRecord;
    }

    private static List<Map<String, String>> getListMapFromWorkbook(Workbook answerStatisticsWb, String columns[]) {
        if(answerStatisticsWb != null){
            Sheet sheet = null;
            Row row = null;
            List<Map<String,String>> list = null;
            String cellData = null;

            //用来存放表中数据
            list = new ArrayList<Map<String,String>>();
            //获取第一个sheet
            sheet = answerStatisticsWb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            //获取第一行
            row = sheet.getRow(0);

            for (int i = 1; i<rownum; i++) {
                Map<String,String> map = new LinkedHashMap<String,String>();
                row = sheet.getRow(i);
                //获取最大列数
                int colnum = row.getPhysicalNumberOfCells();
                if(row !=null){
                    for (int j=0;j<colnum && j<columns.length;j++){
                        cellData = ((String) getCellFormatValue(row.getCell(j)));
                        cellData = cellData.replace(" ", "");
                        cellData = cellData.trim();
                        map.put(columns[j], cellData);
                    }
                }else{
                    break;
                }
                list.add(map);
            }
            return list;
        }

        return null;
    }

    //读取excel
    public static Workbook readExcel(String filePath){
        Workbook wb = null;
        if(filePath==null){
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if(".xls".equals(extString)){
                return wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                return wb = new XSSFWorkbook(is);
            }else{
                return wb = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }
    public static Object getCellFormatValue(Cell cell){
        Object cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
                case Cell.CELL_TYPE_NUMERIC:{
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA:{
                    //判断cell是否为日期格式
                    if(DateUtil.isCellDateFormatted(cell)){
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    }else{
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }
}
