package com.JinRui.fengkong4.entity.mysql;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 个人基本信息MySQL实体
 * 
 * @author JinRui
 */
@Data
@TableName("personal_infos")
public class PersonalInfoMysql {

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
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 证件类型
     */
    @TableField("id_type")
    private String idType;

    /**
     * 证件号码
     */
    @TableField("id_number")
    private String idNumber;

    /**
     * 性别
     */
    @TableField("gender")
    private String gender;

    /**
     * 出生日期
     */
    @TableField("birth_date")
    private String birthDate;

    /**
     * 婚姻状况
     */
    @TableField("marital_status")
    private String maritalStatus;

    /**
     * 学历
     */
    @TableField("education")
    private String education;

    /**
     * 学位
     */
    @TableField("degree")
    private String degree;

    /**
     * 就业状况
     */
    @TableField("employment_status")
    private String employmentStatus;

    /**
     * 国籍
     */
    @TableField("nationality")
    private String nationality;

    /**
     * 电子邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 通讯地址
     */
    @TableField("address")
    private String address;

    /**
     * 手机号码
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 单位名称
     */
    @TableField("company_name")
    private String companyName;

    /**
     * 单位地址
     */
    @TableField("company_address")
    private String companyAddress;

    /**
     * 单位电话
     */
    @TableField("company_phone")
    private String companyPhone;

    /**
     * 职业
     */
    @TableField("occupation")
    private String occupation;

    /**
     * 职务
     */
    @TableField("position")
    private String position;

    /**
     * 年收入
     */
    @TableField("annual_income")
    private String annualIncome;

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
