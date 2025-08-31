package com.JinRui.fengkong4.entity.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 信贷交易信息明细MongoDB实体
 * 
 * @author JinRui
 */
@Data
@Document(collection = "credit_transactions")
public class CreditTransactionMongo {

    /**
     * 主键ID
     */
    @Id
    private String id;

    /**
     * 征信报文ID（关联主表）
     */
    private String creditReportId;

    /**
     * 管理机构
     */
    private String managementOrg;

    /**
     * 账户标识
     */
    private String accountId;

    /**
     * 账户类型
     */
    private String accountType;

    /**
     * 币种
     */
    private String currency;

    /**
     * 开立日期
     */
    private String openDate;

    /**
     * 到期日期
     */
    private String expireDate;

    /**
     * 信用额度/贷款金额
     */
    private Long creditLimit;

    /**
     * 共享授信额度
     */
    private Long sharedCreditLimit;

    /**
     * 账户状态
     */
    private String accountStatus;

    /**
     * 还款频率
     */
    private String repaymentFrequency;

    /**
     * 还款期数
     */
    private Integer repaymentPeriods;

    /**
     * 本月应还款
     */
    private Long currentRepayment;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 构造函数
     */
    public CreditTransactionMongo() {
        this.createTime = new Date();
        this.updateTime = new Date();
    }
}
