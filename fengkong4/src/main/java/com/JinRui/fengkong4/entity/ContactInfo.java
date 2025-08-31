package com.JinRui.fengkong4.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

/**
 * 联系信息模块
 * 
 * @author JinRui
 */
@Data
public class ContactInfo {

    /**
     * 最近5个手机号（格式：号码(最后使用年月)）
     */
    @JacksonXmlProperty(localName = "RecentMobiles")
    private List<String> recentMobiles;

    /**
     * 电子邮箱（用于账单通知）
     */
    @JacksonXmlProperty(localName = "Email")
    private String email;

    /**
     * 通讯地址（当前有效联系地址）
     */
    @JacksonXmlProperty(localName = "MailingAddress")
    private String mailingAddress;

    /**
     * 户籍地址（身份证登记地址）
     */
    @JacksonXmlProperty(localName = "HouseholdAddress")
    private String householdAddress;
}
