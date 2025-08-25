package com.JinRui.fengkong4.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 非信贷交易信息明细
 * 
 * @author JinRui
 */
@Data
public class NonCreditTransaction {

    /**
     * 业务类型 (1-电信缴费, 2-水费缴费, 3-电费缴费, 4-燃气费缴费)
     */
    @JacksonXmlProperty(localName = "BusinessType")
    private String businessType;

    /**
     * 业务机构
     */
    @JacksonXmlProperty(localName = "BusinessOrg")
    private String businessOrg;

    /**
     * 开户日期
     */
    @JacksonXmlProperty(localName = "OpenDate")
    private String openDate;

    /**
     * 账户状态 (1-正常, 2-销户)
     */
    @JacksonXmlProperty(localName = "AccountStatus")
    private String accountStatus;

    /**
     * 最近24个月缴费状态
     */
    @JacksonXmlProperty(localName = "PaymentHistory24Months")
    private String paymentHistory24Months;

    /**
     * 欠费金额
     */
    @JacksonXmlProperty(localName = "OverdueAmount")
    private Long overdueAmount;

    /**
     * 欠费月数
     */
    @JacksonXmlProperty(localName = "OverdueMonths")
    private Integer overdueMonths;
}
