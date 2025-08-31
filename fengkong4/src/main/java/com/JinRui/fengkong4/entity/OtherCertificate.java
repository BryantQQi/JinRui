package com.JinRui.fengkong4.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 其他证件信息
 * 
 * @author JinRui
 */
@Data
public class OtherCertificate {

    /**
     * 证件类型（1-身份证(默认) 2-护照 5-港澳通行证 8-外国人居留证）
     */
    @JacksonXmlProperty(localName = "CertType")
    private String certType;

    /**
     * 证件号码（需脱敏展示）
     */
    @JacksonXmlProperty(localName = "CertNumber")
    private String certNumber;
}
