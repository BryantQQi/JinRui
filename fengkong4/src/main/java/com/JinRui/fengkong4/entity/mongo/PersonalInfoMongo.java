package com.JinRui.fengkong4.entity.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 个人基本信息MongoDB实体
 * 
 * @author JinRui
 */
@Data
@Document(collection = "personal_infos")
public class PersonalInfoMongo {

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
     * 姓名
     */
    private String name;

    /**
     * 证件类型
     */
    private String idType;

    /**
     * 证件号码
     */
    private String idNumber;

    /**
     * 性别
     */
    private String gender;

    /**
     * 出生日期
     */
    private String birthDate;

    /**
     * 婚姻状况
     */
    private String maritalStatus;

    /**
     * 学历
     */
    private String education;

    /**
     * 学位
     */
    private String degree;

    /**
     * 就业状况
     */
    private String employmentStatus;

    /**
     * 国籍
     */
    private String nationality;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 通讯地址
     */
    private String address;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 单位名称
     */
    private String companyName;

    /**
     * 单位地址
     */
    private String companyAddress;

    /**
     * 单位电话
     */
    private String companyPhone;

    /**
     * 职业
     */
    private String occupation;

    /**
     * 职务
     */
    private String position;

    /**
     * 年收入
     */
    private String annualIncome;

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
    public PersonalInfoMongo() {
        this.createTime = new Date();
        this.updateTime = new Date();
    }
}
