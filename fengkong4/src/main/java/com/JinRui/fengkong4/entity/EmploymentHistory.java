package com.JinRui.fengkong4.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 职业历史
 * 
 * @author JinRui
 */
@Data
public class EmploymentHistory {

    /**
     * 工作单位全称
     */
    @JacksonXmlProperty(localName = "Employer")
    private String employer;

    /**
     * 进入本单位年份（YYYY格式）
     */
    @JacksonXmlProperty(localName = "StartYear")
    private String startYear;

    /**
     * 单位性质（11-机关单位 21-私营企业 31-外资企业 41-事业单位）
     */
    @JacksonXmlProperty(localName = "CompanyType")
    private String companyType;

    /**
     * 单位联系电话
     */
    @JacksonXmlProperty(localName = "CompanyPhone")
    private String companyPhone;

    /**
     * 职务名称
     */
    @JacksonXmlProperty(localName = "Position")
    private String position;

    /**
     * 所属行业（自由文本）
     */
    @JacksonXmlProperty(localName = "Industry")
    private String industry;
}
