package com.JinRui.fengkong4.entity.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 非信贷交易信息明细MongoDB实体
 * 
 * @author JinRui
 */
@Data
@Document(collection = "non_credit_transactions")
public class NonCreditTransactionMongo {

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
     * 业务类型
     */
    private String businessType;

    /**
     * 业务机构
     */
    private String businessOrg;

    /**
     * 开户日期
     */
    private String openDate;

    /**
     * 账户状态
     */
    private String accountStatus;

    /**
     * 最近24个月缴费状态
     */
    private String paymentHistory24Months;

    /**
     * 欠费金额
     */
    private Long overdueAmount;

    /**
     * 欠费月数
     */
    private Integer overdueMonths;

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
    public NonCreditTransactionMongo() {
        this.createTime = new Date();
        this.updateTime = new Date();
    }
}
