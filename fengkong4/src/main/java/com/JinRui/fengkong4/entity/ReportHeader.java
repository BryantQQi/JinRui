package com.JinRui.fengkong4.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 征信报文头信息
 * 
 * @author JinRui
 */
@Data
public class ReportHeader {

    /**
     * 报文版本
     */
    @JacksonXmlProperty(localName = "Version")
    private String version;

    /**
     * 报文编号
     */
    @JacksonXmlProperty(localName = "ReportNumber")
    private String reportNumber;

    /**
     * 生成时间
     */
    @JacksonXmlProperty(localName = "GenerateTime")
    private String generateTime;

    /**
     * 机构代码
     */
    @JacksonXmlProperty(localName = "InstitutionCode")
    private String institutionCode;

    /**
     * 查询原因
     */
    @JacksonXmlProperty(localName = "QueryReason")
    private String queryReason;

    /**
     * 报文类型 (1-个人征信报告)
     */
    @JacksonXmlProperty(localName = "ReportType")
    private String reportType;
}
