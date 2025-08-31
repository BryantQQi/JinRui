package com.JinRui.fengkong4.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 居住历史
 * 
 * @author JinRui
 */
@Data
public class ResidenceHistory {

    /**
     * 居住地址全称
     */
    @JacksonXmlProperty(localName = "Address")
    private String address;

    /**
     * 住宅固定电话
     */
    @JacksonXmlProperty(localName = "ResidencePhone")
    private String residencePhone;

    /**
     * 地址更新时间（YYYY-MM格式）
     */
    @JacksonXmlProperty(localName = "UpdateDate")
    private String updateDate;
}
