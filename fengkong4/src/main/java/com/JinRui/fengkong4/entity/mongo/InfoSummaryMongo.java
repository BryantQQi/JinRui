package com.JinRui.fengkong4.entity.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 信息概要MongoDB实体
 * 
 * @author JinRui
 */
@Data
@Document(collection = "info_summaries")
public class InfoSummaryMongo {

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
     * 信贷交易账户数
     */
    private Integer creditAccountCount;

    /**
     * 贷记卡账户数
     */
    private Integer creditCardCount;

    /**
     * 准贷记卡账户数
     */
    private Integer quasiCreditCardCount;

    /**
     * 贷款账户数
     */
    private Integer loanAccountCount;

    /**
     * 信用额度总额
     */
    private Long totalCreditLimit;

    /**
     * 已使用信用额度
     */
    private Long usedCreditLimit;

    /**
     * 最近6个月平均应还款
     */
    private Long avgRepaymentLast6Months;

    /**
     * 逾期账户数
     */
    private Integer overdueAccountCount;

    /**
     * 逾期金额
     */
    private Long overdueAmount;

    /**
     * 呆账账户数
     */
    private Integer badDebtAccountCount;

    /**
     * 呆账金额
     */
    private Long badDebtAmount;

    /**
     * 最近5年内贷款逾期次数
     */
    private Integer loanOverdueCountLast5Years;

    /**
     * 最近2年内贷记卡逾期次数
     */
    private Integer creditCardOverdueCountLast2Years;

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
    public InfoSummaryMongo() {
        this.createTime = new Date();
        this.updateTime = new Date();
    }
}
