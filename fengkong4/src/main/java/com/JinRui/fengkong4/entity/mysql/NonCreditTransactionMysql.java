package com.JinRui.fengkong4.entity.mysql;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 非信贷交易信息明细MySQL实体
 * 
 * @author JinRui
 */
@Data
@TableName("non_credit_transactions")
public class NonCreditTransactionMysql {

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
     * 业务类型
     */
    @TableField("business_type")
    private String businessType;

    /**
     * 业务机构
     */
    @TableField("business_org")
    private String businessOrg;

    /**
     * 开户日期
     */
    @TableField("open_date")
    private String openDate;

    /**
     * 账户状态
     */
    @TableField("account_status")
    private String accountStatus;

    /**
     * 最近24个月缴费状态
     */
    @TableField("payment_history_24_months")
    private String paymentHistory24Months;

    /**
     * 欠费金额
     */
    @TableField("overdue_amount")
    private Long overdueAmount;

    /**
     * 欠费月数
     */
    @TableField("overdue_months")
    private Integer overdueMonths;

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
