package com.JinRui.fengkong4.entity.mysql;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 公共信息明细MySQL实体
 * 
 * @author JinRui
 */
@Data
@TableName("public_infos")
public class PublicInfoMysql {

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
     * 信息类型
     */
    @TableField("info_type")
    private String infoType;

    /**
     * 记录机关
     */
    @TableField("record_org")
    private String recordOrg;

    /**
     * 记录日期
     */
    @TableField("record_date")
    private String recordDate;

    /**
     * 公布日期
     */
    @TableField("publish_date")
    private String publishDate;

    /**
     * 结案方式
     */
    @TableField("closure_type")
    private String closureType;

    /**
     * 结案日期
     */
    @TableField("closure_date")
    private String closureDate;

    /**
     * 涉及金额
     */
    @TableField("amount")
    private Long amount;

    /**
     * 内容描述
     */
    @TableField("description")
    private String description;

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
