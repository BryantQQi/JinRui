package com.JinRui.fengkong4.entity.mysql;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 查询记录MySQL实体
 * 
 * @author JinRui
 */
@Data
@TableName("query_records")
public class QueryRecordMysql {

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
     * 查询日期
     */
    @TableField("query_date")
    private String queryDate;

    /**
     * 查询机构
     */
    @TableField("query_org")
    private String queryOrg;

    /**
     * 查询原因
     */
    @TableField("query_reason")
    private String queryReason;

    /**
     * 查询类型
     */
    @TableField("query_type")
    private String queryType;

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
