package com.JinRui.fengkong4.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 配偶信息
 * 
 * @author JinRui
 */
@Data
public class SpouseInfo {

    /**
     * 配偶姓名
     */
    @JacksonXmlProperty(localName = "Name")
    private String name;

    /**
     * 配偶证件类型（1-身份证 2-护照 5-港澳通行证 8-外国人居留证）
     */
    @JacksonXmlProperty(localName = "CertType")
    private String certType;

    /**
     * 配偶证件号码（脱敏）
     */
    @JacksonXmlProperty(localName = "CertNumber")
    private String certNumber;

    /**
     * 配偶工作单位
     */
    @JacksonXmlProperty(localName = "Employer")
    private String employer;

    /**
     * 配偶联系电话
     */
    @JacksonXmlProperty(localName = "Contact")
    private String contact;
}
