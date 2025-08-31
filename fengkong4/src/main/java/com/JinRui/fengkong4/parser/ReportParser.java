package com.JinRui.fengkong4.parser;

import com.JinRui.fengkong4.entity.CreditReport;
import com.JinRui.fengkong4.entity.TreeNode;

/**
 * 征信报文解析器接口
 * 
 * @author JinRui
 */
public interface ReportParser {
    
    /**
     * 解析报文数据为TreeNode
     * 
     * @param reportData 报文数据（XML或JSON字符串）
     * @param format 数据格式（XML或JSON）
     * @return TreeNode 解析后的多叉树结构
     * @throws ParseException 解析异常
     */
    TreeNode parse(String reportData, String format) throws ParseException;
    
    /**
     * 解析报文数据为CreditReport对象
     * 
     * @param reportData 报文数据（XML或JSON字符串）
     * @param format 数据格式（XML或JSON）
     * @return CreditReport 解析后的征信报文对象
     * @throws ParseException 解析异常
     */
    CreditReport parseToCreditReport(String reportData, String format) throws ParseException;
    
    /**
     * 获取支持的格式
     * 
     * @return 支持的格式字符串
     */
    String getSupportedFormat();
    
    /**
     * 验证数据格式是否正确
     * 
     * @param reportData 报文数据
     * @return 是否为有效格式
     */
    boolean isValidFormat(String reportData);
}
