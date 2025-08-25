package com.JinRui.fengkong4.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 公共信息明细
 * 
 * @author JinRui
 */
@Data
public class PublicInfo {

    /**
     * 信息类型 (1-欠税记录, 2-民事判决记录, 3-强制执行记录, 4-行政处罚记录)
     */
    @JacksonXmlProperty(localName = "InfoType")
    private String infoType;

    /**
     * 记录机关
     */
    @JacksonXmlProperty(localName = "RecordOrg")
    private String recordOrg;

    /**
     * 记录日期
     */
    @JacksonXmlProperty(localName = "RecordDate")
    private String recordDate;

    /**
     * 公布日期
     */
    @JacksonXmlProperty(localName = "PublishDate")
    private String publishDate;

    /**
     * 结案方式 (1-已结案, 2-未结案)
     */
    @JacksonXmlProperty(localName = "ClosureType")
    private String closureType;

    /**
     * 结案日期
     */
    @JacksonXmlProperty(localName = "ClosureDate")
    private String closureDate;

    /**
     * 涉及金额
     */
    @JacksonXmlProperty(localName = "Amount")
    private Long amount;

    /**
     * 内容描述
     */
    @JacksonXmlProperty(localName = "Description")
    private String description;
}
