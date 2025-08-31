package com.JinRui.fengkong4.entity.mysql;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 信息概要MySQL实体
 * 
 * @author JinRui
 */
@Data
@TableName("info_summaries")
public class InfoSummaryMysql {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 征信报文ID（关联主表）
     */
    @TableField("credit_report_id")
    private String creditReportId;

    /**
     * 信贷交易账户数
     */
    @TableField("credit_account_count")
    private Integer creditAccountCount;

    /**
     * 贷记卡账户数
     */
    @TableField("credit_card_count")
    private Integer creditCardCount;

    /**
     * 准贷记卡账户数
     */
    @TableField("quasi_credit_card_count")
    private Integer quasiCreditCardCount;

    /**
     * 贷款账户数
     */
    @TableField("loan_account_count")
    private Integer loanAccountCount;

    /**
     * 信用额度总额
     */
    @TableField("total_credit_limit")
    private Long totalCreditLimit;

    /**
     * 已使用信用额度
     */
    @TableField("used_credit_limit")
    private Long usedCreditLimit;

    /**
     * 最近6个月平均应还款
     */
    @TableField("avg_repayment_last_6_months")
    private Long avgRepaymentLast6Months;

    /**
     * 逾期账户数
     */
    @TableField("overdue_account_count")
    private Integer overdueAccountCount;

    /**
     * 逾期金额
     */
    @TableField("overdue_amount")
    private Long overdueAmount;

    /**
     * 呆账账户数
     */
    @TableField("bad_debt_account_count")
    private Integer badDebtAccountCount;

    /**
     * 呆账金额
     */
    @TableField("bad_debt_amount")
    private Long badDebtAmount;

    /**
     * 最近5年内贷款逾期次数
     */
    @TableField("loan_overdue_count_last_5_years")
    private Integer loanOverdueCountLast5Years;

    /**
     * 最近2年内贷记卡逾期次数
     */
    @TableField("credit_card_overdue_count_last_2_years")
    private Integer creditCardOverdueCountLast2Years;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 逻辑删除标识
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
