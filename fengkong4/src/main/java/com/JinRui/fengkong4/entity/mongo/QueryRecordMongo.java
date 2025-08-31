package com.JinRui.fengkong4.entity.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 查询记录MongoDB实体
 * 
 * @author JinRui
 */
@Data
@Document(collection = "query_records")
public class QueryRecordMongo {

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
     * 查询日期
     */
    private String queryDate;

    /**
     * 查询机构
     */
    private String queryOrg;

    /**
     * 查询原因
     */
    private String queryReason;

    /**
     * 查询类型
     */
    private String queryType;

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
    public QueryRecordMongo() {
        this.createTime = new Date();
        this.updateTime = new Date();
    }
}
