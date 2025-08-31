package com.JinRui.fengkong4.entity.mysql;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 征信报文主记录MySQL实体
 * 
 * @author JinRui
 */
@Data
@TableName("credit_reports")
public class CreditReportMysql {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 征信报文ID（用于关联子表）
     */
    @TableField("credit_report_id")
    private String creditReportId;

    /**
     * 身份证号码
     */
    @TableField("id_number")
    private String idNumber;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 报文编号
     */
    @TableField("report_number")
    private String reportNumber;

    /**
     * 生成时间
     */
    @TableField("generate_time")
    private String generateTime;

    /**
     * 机构代码
     */
    @TableField("institution_code")
    private String institutionCode;

    /**
     * 查询原因
     */
    @TableField("query_reason")
    private String queryReason;

    /**
     * 报文类型
     */
    @TableField("report_type")
    private String reportType;

    /**
     * 报告查询时间
     */
    @TableField("report_query_time")
    private String reportQueryTime;

    /**
     * 是否查询到有效征信记录
     */
    @TableField("has_valid_credit_info")
    private Boolean hasValidCreditInfo;

    /**
     * 首次贷款/贷记卡距今月份数
     */
    @TableField("first_credit_month_diff")
    private Integer firstCreditMonthDiff;

    /**
     * 征信评分
     */
    @TableField("credit_score")
    private Integer creditScore;

    /**
     * 评分时间
     */
    @TableField("score_time")
    private Date scoreTime;

    /**
     * 评分状态（PENDING-待评分，PROCESSING-评分中，COMPLETED-已完成，FAILED-失败）
     */
    @TableField("score_status")
    private String scoreStatus;

    /**
     * 大模型响应时间（毫秒）
     */
    @TableField("ai_response_time")
    private Long aiResponseTime;

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
