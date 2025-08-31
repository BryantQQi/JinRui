package com.JinRui.fengkong4.entity.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 公共信息明细MongoDB实体
 * 
 * @author JinRui
 */
@Data
@Document(collection = "public_infos")
public class PublicInfoMongo {

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
     * 信息类型
     */
    private String infoType;

    /**
     * 记录机关
     */
    private String recordOrg;

    /**
     * 记录日期
     */
    private String recordDate;

    /**
     * 公布日期
     */
    private String publishDate;

    /**
     * 结案方式
     */
    private String closureType;

    /**
     * 结案日期
     */
    private String closureDate;

    /**
     * 涉及金额
     */
    private Long amount;

    /**
     * 内容描述
     */
    private String description;

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
    public PublicInfoMongo() {
        this.createTime = new Date();
        this.updateTime = new Date();
    }
}
