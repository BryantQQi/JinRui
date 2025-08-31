package com.JinRui.fengkong4.service;

import com.JinRui.fengkong4.entity.CreditReport;
import com.JinRui.fengkong4.parser.ParseException;

/**
 * 征信报文解析服务接口
 * 
 * @author JinRui
 */
public interface CreditReportParseService {

    /**
     * 通过身份证号码和姓名获取并解析征信报文为CreditReport对象
     * 
     * @param idCard 身份证号码
     * @param name 姓名
     * @return CreditReport 征信报文对象
     * @throws ParseException 解析异常
     */
    CreditReport getCreditReportByIdAndName(String idCard, String name) throws ParseException;

    /**
     * 解析征信报文数据为CreditReport对象
     * 
     * @param reportData 报文数据
     * @return CreditReport 征信报文对象
     * @throws ParseException 解析异常
     */
    CreditReport parseCreditReport(String reportData) throws ParseException;

    /**
     * 根据指定格式解析征信报文数据为CreditReport对象
     * 
     * @param reportData 报文数据
     * @param format 数据格式（XML或JSON）
     * @return CreditReport 征信报文对象
     * @throws ParseException 解析异常
     */
    CreditReport parseCreditReportByFormat(String reportData, String format) throws ParseException;
}
