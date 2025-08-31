package com.JinRui.fengkong4.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

/**
 * 二代征信报文主体
 * 
 * @author JinRui
 */
@Data
@JacksonXmlRootElement(localName = "CreditReport")
public class CreditReport {

    /**
     * 报文头
     */
    @JacksonXmlProperty(localName = "Header")
    private ReportHeader header;

    /**
     * 个人基本信息
     */
    @JacksonXmlProperty(localName = "PersonalInfo")
    private PersonalInfo personalInfo;

    /**
     * 信息概要
     */
    @JacksonXmlProperty(localName = "InfoSummary")
    private InfoSummary infoSummary;

    /**
     * 信贷交易信息明细
     */
    @JacksonXmlElementWrapper(localName = "CreditTransactions")
    @JacksonXmlProperty(localName = "CreditTransaction")
    private List<CreditTransaction> creditTransactions;

    /**
     * 非信贷交易信息明细
     */
    @JacksonXmlElementWrapper(localName = "NonCreditTransactions")
    @JacksonXmlProperty(localName = "NonCreditTransaction")
    private List<NonCreditTransaction> nonCreditTransactions;

    /**
     * 公共信息明细
     */
    @JacksonXmlElementWrapper(localName = "PublicInfos")
    @JacksonXmlProperty(localName = "PublicInfo")
    private List<PublicInfo> publicInfos;

    /**
     * 查询记录
     */
    @JacksonXmlElementWrapper(localName = "QueryRecords")
    @JacksonXmlProperty(localName = "QueryRecord")
    private List<QueryRecord> queryRecords;

    /**
     * 标注及声明信息
     */
    @JacksonXmlProperty(localName = "StatementInfo")
    private StatementInfo statementInfo;

    /**
     * 防欺诈提示
     */
    @JacksonXmlProperty(localName = "FraudWarnings")
    private FraudWarnings fraudWarnings;

    /**
     * 职业历史
     */
    @JacksonXmlProperty(localName = "EmploymentHistory")
    private List<EmploymentHistory> employmentHistory;

    /**
     * 居住历史
     */
    @JacksonXmlProperty(localName = "ResidenceHistory")
    private List<ResidenceHistory> residenceHistory;

    /**
     * 信用概要增强
     */
    @JacksonXmlProperty(localName = "CreditSummary")
    private CreditSummary creditSummary;

    /**
     * 授信额度
     */
    @JacksonXmlProperty(localName = "CreditLimits")
    private CreditLimits creditLimits;
}
