package com.JinRui.fengkong4.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 标注及声明信息
 * 
 * @author JinRui
 */
@Data
public class StatementInfo {

    /**
     * 异议标注
     */
    @JacksonXmlProperty(localName = "DisputeNote")
    private String disputeNote;

    /**
     * 本人声明
     */
    @JacksonXmlProperty(localName = "PersonalStatement")
    private String personalStatement;

    /**
     * 异议处理情况
     */
    @JacksonXmlProperty(localName = "DisputeHandling")
    private String disputeHandling;
}
