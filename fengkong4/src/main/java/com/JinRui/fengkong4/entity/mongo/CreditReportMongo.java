package com.JinRui.fengkong4.entity.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 征信报文主记录MongoDB实体
 * 
 * @author JinRui
 */
@Data
@Document(collection = "credit_reports")
public class CreditReportMongo {

    /**
     * 主键ID
     */
    @Id
    private String id;

    /**
     * 征信报文ID（用于关联子表）
     */
    private String creditReportId;

    /**
     * 身份证号码
     */
    private String idNumber;

    /**
     * 姓名
     */
    private String name;

    /**
     * 报文编号
     */
    private String reportNumber;

    /**
     * 生成时间
     */
    private String generateTime;

    /**
     * 机构代码
     */
    private String institutionCode;

    /**
     * 查询原因
     */
    private String queryReason;

    /**
     * 报文类型
     */
    private String reportType;

    /**
     * 报告查询时间
     */
    private String reportQueryTime;

    /**
     * 是否查询到有效征信记录
     */
    private Boolean hasValidCreditInfo;

    /**
     * 首次贷款/贷记卡距今月份数
     */
    private Integer firstCreditMonthDiff;

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
    public CreditReportMongo() {
        this.createTime = new Date();
        this.updateTime = new Date();
    }
}
