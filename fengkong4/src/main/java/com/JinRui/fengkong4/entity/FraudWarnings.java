package com.JinRui.fengkong4.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 防欺诈提示
 * 
 * @author JinRui
 */
@Data
public class FraudWarnings {

    /**
     * 是否存在防欺诈标记（true时需人工复核）
     */
    @JacksonXmlProperty(localName = "HasFraudAlert")
    private Boolean hasFraudAlert;

    /**
     * 防欺诈提示生效日期（YYYY-MM-DD）
     */
    @JacksonXmlProperty(localName = "AlertStartDate")
    private String alertStartDate;

    /**
     * 防欺诈提示截止日期（YYYY-MM-DD）
     */
    @JacksonXmlProperty(localName = "AlertEndDate")
    private String alertEndDate;

    /**
     * 是否存在异议标注（true表示用户对报告数据有争议）
     */
    @JacksonXmlProperty(localName = "HasDispute")
    private Boolean hasDispute;
}
