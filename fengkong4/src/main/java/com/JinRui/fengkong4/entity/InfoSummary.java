package com.JinRui.fengkong4.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 信息概要
 * 
 * @author JinRui
 */
@Data
public class InfoSummary {

    /**
     * 信贷交易账户数
     */
    @JacksonXmlProperty(localName = "CreditAccountCount")
    private Integer creditAccountCount;

    /**
     * 贷记卡账户数
     */
    @JacksonXmlProperty(localName = "CreditCardCount")
    private Integer creditCardCount;

    /**
     * 准贷记卡账户数
     */
    @JacksonXmlProperty(localName = "QuasiCreditCardCount")
    private Integer quasiCreditCardCount;

    /**
     * 贷款账户数
     */
    @JacksonXmlProperty(localName = "LoanAccountCount")
    private Integer loanAccountCount;

    /**
     * 信用额度总额
     */
    @JacksonXmlProperty(localName = "TotalCreditLimit")
    private Long totalCreditLimit;

    /**
     * 已使用信用额度
     */
    @JacksonXmlProperty(localName = "UsedCreditLimit")
    private Long usedCreditLimit;

    /**
     * 最近6个月平均应还款
     */
    @JacksonXmlProperty(localName = "AvgRepaymentLast6Months")
    private Long avgRepaymentLast6Months;

    /**
     * 逾期账户数
     */
    @JacksonXmlProperty(localName = "OverdueAccountCount")
    private Integer overdueAccountCount;

    /**
     * 逾期金额
     */
    @JacksonXmlProperty(localName = "OverdueAmount")
    private Long overdueAmount;

    /**
     * 呆账账户数
     */
    @JacksonXmlProperty(localName = "BadDebtAccountCount")
    private Integer badDebtAccountCount;

    /**
     * 呆账金额
     */
    @JacksonXmlProperty(localName = "BadDebtAmount")
    private Long badDebtAmount;

    /**
     * 最近5年内贷款逾期次数
     */
    @JacksonXmlProperty(localName = "LoanOverdueCountLast5Years")
    private Integer loanOverdueCountLast5Years;

    /**
     * 最近2年内贷记卡逾期次数
     */
    @JacksonXmlProperty(localName = "CreditCardOverdueCountLast2Years")
    private Integer creditCardOverdueCountLast2Years;
}
