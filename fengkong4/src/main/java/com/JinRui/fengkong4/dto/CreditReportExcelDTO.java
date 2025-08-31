package com.JinRui.fengkong4.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 征信报告Excel导出DTO
 * 
 * @author JinRui
 */
@Data
public class CreditReportExcelDTO {

    @ExcelProperty(value = "主键ID", index = 0)
    private String id;

    @ExcelProperty(value = "征信报文ID", index = 1)
    private String creditReportId;

    @ExcelProperty(value = "身份证号码", index = 2)
    private String idNumber;

    @ExcelProperty(value = "姓名", index = 3)
    private String name;

    @ExcelProperty(value = "报文编号", index = 4)
    private String reportNumber;

    @ExcelProperty(value = "生成时间", index = 5)
    private String generateTime;

    @ExcelProperty(value = "机构代码", index = 6)
    private String institutionCode;

    @ExcelProperty(value = "查询原因", index = 7)
    private String queryReason;

    @ExcelProperty(value = "报文类型", index = 8)
    private String reportType;

    @ExcelProperty(value = "报告查询时间", index = 9)
    private String reportQueryTime;

    @ExcelProperty(value = "是否有效征信记录", index = 10)
    private String hasValidCreditInfo;

    @ExcelProperty(value = "首次贷款距今月份数", index = 11)
    private Integer firstCreditMonthDiff;

    @ExcelProperty(value = "征信评分", index = 12)
    private Integer creditScore;

    @ExcelProperty(value = "评分时间", index = 13)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date scoreTime;

    @ExcelProperty(value = "评分状态", index = 14)
    private String scoreStatus;

    @ExcelProperty(value = "AI响应时间(毫秒)", index = 15)
    private Long aiResponseTime;

    @ExcelProperty(value = "创建时间", index = 16)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ExcelProperty(value = "更新时间", index = 17)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 数据脱敏处理工具方法
     */
    public static class DataMaskUtil {
        
        /**
         * 身份证号脱敏：保留前6位和后2位
         */
        public static String maskIdNumber(String idNumber) {
            if (idNumber == null || idNumber.length() < 8) {
                return idNumber;
            }
            return idNumber.substring(0, 6) + "********" + idNumber.substring(idNumber.length() - 2);
        }

        /**
         * 姓名脱敏：保留姓氏
         */
        public static String maskName(String name) {
            if (name == null || name.length() <= 1) {
                return name;
            }
            StringBuilder masked = new StringBuilder();
            masked.append(name.charAt(0));
            for (int i = 1; i < name.length(); i++) {
                masked.append("*");
            }
            return masked.toString();
        }

        /**
         * 布尔值转换为中文
         */
        public static String booleanToChinese(Boolean value) {
            if (value == null) {
                return "未知";
            }
            return value ? "是" : "否";
        }

        /**
         * 评分状态转换为中文
         */
        public static String scoreStatusToChinese(String status) {
            if (status == null) {
                return "未知";
            }
            switch (status.toUpperCase()) {
                case "PENDING":
                    return "待评分";
                case "PROCESSING":
                    return "评分中";
                case "COMPLETED":
                    return "已完成";
                case "FAILED":
                    return "失败";
                default:
                    return status;
            }
        }
    }
}
