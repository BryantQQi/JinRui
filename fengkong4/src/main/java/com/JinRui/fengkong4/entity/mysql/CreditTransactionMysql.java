package com.JinRui.fengkong4.entity.mysql;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 信贷交易信息明细MySQL实体
 * 
 * @author JinRui
 */
@Data
@TableName("credit_transactions")
public class CreditTransactionMysql {

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
     * 管理机构
     */
    @TableField("management_org")
    private String managementOrg;

    /**
     * 账户标识
     */
    @TableField("account_id")
    private String accountId;

    /**
     * 账户类型
     */
    @TableField("account_type")
    private String accountType;

    /**
     * 币种
     */
    @TableField("currency")
    private String currency;

    /**
     * 开立日期
     */
    @TableField("open_date")
    private String openDate;

    /**
     * 到期日期
     */
    @TableField("expire_date")
    private String expireDate;

    /**
     * 信用额度/贷款金额
     */
    @TableField("credit_limit")
    private Long creditLimit;

    /**
     * 共享授信额度
     */
    @TableField("shared_credit_limit")
    private Long sharedCreditLimit;

    /**
     * 账户状态
     */
    @TableField("account_status")
    private String accountStatus;

    /**
     * 还款频率
     */
    @TableField("repayment_frequency")
    private String repaymentFrequency;

    /**
     * 还款期数
     */
    @TableField("repayment_periods")
    private Integer repaymentPeriods;

    /**
     * 本月应还款
     */
    @TableField("current_repayment")
    private Long currentRepayment;

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
