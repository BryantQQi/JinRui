package com.JinRui.fengkong4.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 信贷交易信息明细
 * 
 * @author JinRui
 */
@Data
public class CreditTransaction {

    /**
     * 管理机构
     */
    @JacksonXmlProperty(localName = "ManagementOrg")
    private String managementOrg;

    /**
     * 账户标识
     */
    @JacksonXmlProperty(localName = "AccountId")
    private String accountId;

    /**
     * 账户类型 (1-贷记卡, 2-准贷记卡, 3-贷款)
     */
    @JacksonXmlProperty(localName = "AccountType")
    private String accountType;

    /**
     * 币种 (CNY-人民币)
     */
    @JacksonXmlProperty(localName = "Currency")
    private String currency;

    /**
     * 开立日期
     */
    @JacksonXmlProperty(localName = "OpenDate")
    private String openDate;

    /**
     * 到期日期
     */
    @JacksonXmlProperty(localName = "ExpireDate")
    private String expireDate;

    /**
     * 信用额度/贷款金额
     */
    @JacksonXmlProperty(localName = "CreditLimit")
    private Long creditLimit;

    /**
     * 共享授信额度
     */
    @JacksonXmlProperty(localName = "SharedCreditLimit")
    private Long sharedCreditLimit;

    /**
     * 账户状态 (1-正常, 2-冻结, 3-止付, 4-呆账, 5-转出, 9-销户)
     */
    @JacksonXmlProperty(localName = "AccountStatus")
    private String accountStatus;

    /**
     * 还款频率 (1-月, 2-季, 3-半年, 4-年, 5-不定期, 9-一次性)
     */
    @JacksonXmlProperty(localName = "RepaymentFrequency")
    private String repaymentFrequency;

    /**
     * 还款期数
     */
    @JacksonXmlProperty(localName = "RepaymentPeriods")
    private Integer repaymentPeriods;

    /**
     * 本月应还款
     */
    @JacksonXmlProperty(localName = "CurrentRepayment")
    private Long currentRepayment;

    /**
     * 账户余额/本金余额
     */
    @JacksonXmlProperty(localName = "Balance")
    private Long balance;

    /**
     * 剩余还款期数
     */
    @JacksonXmlProperty(localName = "RemainingPeriods")
    private Integer remainingPeriods;

    /**
     * 本月实还款
     */
    @JacksonXmlProperty(localName = "ActualRepayment")
    private Long actualRepayment;

    /**
     * 当前逾期期数
     */
    @JacksonXmlProperty(localName = "CurrentOverduePeriods")
    private Integer currentOverduePeriods;

    /**
     * 当前逾期总额
     */
    @JacksonXmlProperty(localName = "CurrentOverdueAmount")
    private Long currentOverdueAmount;

    /**
     * 逾期31-60天未归还贷款本金
     */
    @JacksonXmlProperty(localName = "Overdue31To60Days")
    private Long overdue31To60Days;

    /**
     * 逾期61-90天未归还贷款本金
     */
    @JacksonXmlProperty(localName = "Overdue61To90Days")
    private Long overdue61To90Days;

    /**
     * 逾期91-180天未归还贷款本金
     */
    @JacksonXmlProperty(localName = "Overdue91To180Days")
    private Long overdue91To180Days;

    /**
     * 逾期180天以上未归还贷款本金
     */
    @JacksonXmlProperty(localName = "OverdueOver180Days")
    private Long overdueOver180Days;

    /**
     * 最近24个月还款状态记录
     */
    @JacksonXmlProperty(localName = "RepaymentHistory24Months")
    private String repaymentHistory24Months;
}
