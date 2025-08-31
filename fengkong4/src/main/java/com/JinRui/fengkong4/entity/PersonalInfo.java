package com.JinRui.fengkong4.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

/**
 * 个人基本信息
 * 
 * @author JinRui
 */
@Data
public class PersonalInfo {

    /**
     * 姓名
     */
    @JacksonXmlProperty(localName = "Name")
    private String name;

    /**
     * 证件类型 (1-身份证)
     */
    @JacksonXmlProperty(localName = "IdType")
    private String idType;

    /**
     * 证件号码
     */
    @JacksonXmlProperty(localName = "IdNumber")
    private String idNumber;

    /**
     * 性别 (1-男, 2-女)
     */
    @JacksonXmlProperty(localName = "Gender")
    private String gender;

    /**
     * 出生日期
     */
    @JacksonXmlProperty(localName = "BirthDate")
    private String birthDate;

    /**
     * 婚姻状况 (1-未婚, 2-已婚, 3-离异, 4-丧偶, 9-未知)
     */
    @JacksonXmlProperty(localName = "MaritalStatus")
    private String maritalStatus;

    /**
     * 学历 (1-研究生及以上, 2-大学本科, 3-大学专科和专科学校, 4-中等专业学校, 5-技工学校, 6-高中, 7-初中, 8-小学, 9-文盲或半文盲, 99-未知)
     */
    @JacksonXmlProperty(localName = "Education")
    private String education;

    /**
     * 学位 (1-博士, 2-硕士, 3-学士, 4-其他, 9-未知)
     */
    @JacksonXmlProperty(localName = "Degree")
    private String degree;

    /**
     * 就业状况 (1-就业, 2-自雇, 3-失业, 4-退休, 5-其他, 9-未知)
     */
    @JacksonXmlProperty(localName = "EmploymentStatus")
    private String employmentStatus;

    /**
     * 国籍
     */
    @JacksonXmlProperty(localName = "Nationality")
    private String nationality;

    /**
     * 电子邮箱
     */
    @JacksonXmlProperty(localName = "Email")
    private String email;

    /**
     * 通讯地址
     */
    @JacksonXmlProperty(localName = "Address")
    private String address;

    /**
     * 手机号码
     */
    @JacksonXmlProperty(localName = "Mobile")
    private String mobile;

    /**
     * 单位名称
     */
    @JacksonXmlProperty(localName = "CompanyName")
    private String companyName;

    /**
     * 单位地址
     */
    @JacksonXmlProperty(localName = "CompanyAddress")
    private String companyAddress;

    /**
     * 单位电话
     */
    @JacksonXmlProperty(localName = "CompanyPhone")
    private String companyPhone;

    /**
     * 职业
     */
    @JacksonXmlProperty(localName = "Occupation")
    private String occupation;

    /**
     * 职务
     */
    @JacksonXmlProperty(localName = "Position")
    private String position;

    /**
     * 年收入
     */
    @JacksonXmlProperty(localName = "AnnualIncome")
    private String annualIncome;

    /**
     * 联系信息模块
     */
    @JacksonXmlProperty(localName = "ContactInfo")
    private ContactInfo contactInfo;

    /**
     * 配偶信息（仅当marital_status=20时返回有效数据）
     */
    @JacksonXmlProperty(localName = "SpouseInfo")
    private SpouseInfo spouseInfo;

    /**
     * 其他证件列表（非身份证证件补充）
     */
    @JacksonXmlProperty(localName = "OtherCertificates")
    private List<OtherCertificate> otherCertificates;
}
