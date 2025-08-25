package com.JinRui.fengkong4.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 查询记录
 * 
 * @author JinRui
 */
@Data
public class QueryRecord {

    /**
     * 查询日期
     */
    @JacksonXmlProperty(localName = "QueryDate")
    private String queryDate;

    /**
     * 查询机构
     */
    @JacksonXmlProperty(localName = "QueryOrg")
    private String queryOrg;

    /**
     * 查询原因 (01-贷款审批, 02-信用卡审批, 03-担保资格审查, 04-贷后管理, 05-本人查询, 99-其他)
     */
    @JacksonXmlProperty(localName = "QueryReason")
    private String queryReason;

    /**
     * 查询类型 (1-机构查询, 2-本人查询)
     */
    @JacksonXmlProperty(localName = "QueryType")
    private String queryType;
}
