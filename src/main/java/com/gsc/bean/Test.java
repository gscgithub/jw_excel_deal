package com.gsc.bean;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Test {

    public static void main(String[] args) {
        System.out.println("220kV雅布赖集控站".equalsIgnoreCase("220KV雅布赖集控站"));
        System.out.println("220kV雅布赖集控站".toUpperCase().equals("220KV雅布赖集控站"));
    }

    public static void fun1() {
        //测试创建单元格    public static void main(String[] args) throws Exception {        //
        // 1.创建workbook工作簿
        Workbook wb = new XSSFWorkbook();        //2.创建表单Sheet
        // Sheet sheet = wb.createSheet("test");        //3.创建行对象，从0开始
        // Row row = sheet.createRow(3);        //4.创建单元格，从0开始
        // Cell cell = row.createCell(0);        //5.单元格写入数据
        // cell.setCellValue("传智播客");        //6.文件流
        // FileOutputStream fos = new FileOutputStream("E:\\test.xlsx");        //7.写入文件
        // wb.write(fos);
        // fos.close();   }

    }
}
