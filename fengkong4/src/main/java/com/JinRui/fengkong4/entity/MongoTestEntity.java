package com.JinRui.fengkong4.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * MongoDB测试实体类
 * 
 * @author JinRui
 */
@Data
@Document(collection = "mongo_test")
public class MongoTestEntity {

    /**
     * 主键ID
     */
    @Id
    private String id;

    /**
     * 测试名称
     */
    private String name;

    /**
     * 测试数据内容
     */
    private String data;

    /**
     * 测试数据内容2
     */
    private String data2;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 构造函数
     */
    public MongoTestEntity() {
        this.createTime = new Date();
    }

    /**
     * 带参构造函数
     */
    public MongoTestEntity(String name, String data) {
        this.name = name;
        this.data = data;
        this.createTime = new Date();
    }
}
