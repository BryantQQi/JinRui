package com.JinRui.fengkong4.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 授信额度
 * 
 * @author JinRui
 */
@Data
public class CreditLimits {

    /**
     * 各类型账户最大授信额度
     */
    @JacksonXmlProperty(localName = "MaxLimits")
    private LimitInfo maxLimits;

    /**
     * 各类型账户最小授信额度
     */
    @JacksonXmlProperty(localName = "MinLimits")
    private LimitInfo minLimits;

    /**
     * 次高授信额度（用于分析额度分布）
     */
    @JacksonXmlProperty(localName = "SecondMaxLimits")
    private LimitInfo secondMaxLimits;

    /**
     * 时间窗口统计指标（反映额度变化趋势）
     */
    @JacksonXmlProperty(localName = "TimeWindowStats")
    private TimeWindowStats timeWindowStats;

    @Data
    public static class LimitInfo {
        /**
         * 非循环贷最高授信（如房贷、车贷）
         */
        @JacksonXmlProperty(localName = "NonRevolving")
        private Long nonRevolving;

        /**
         * 信用卡最高授信额度
         */
        @JacksonXmlProperty(localName = "CreditCard")
        private Long creditCard;

        /**
         * 循环贷最高授信（如信用贷）
         */
        @JacksonXmlProperty(localName = "Revolving")
        private Long revolving;

        /**
         * 循环额度下分账户最高授信
         */
        @JacksonXmlProperty(localName = "RevolvingSub")
        private Long revolvingSub;

        /**
         * 所有账户中的最大授信值
         */
        @JacksonXmlProperty(localName = "OverallMax")
        private Long overallMax;

        /**
         * 消费金融类机构最低放款额度
         */
        @JacksonXmlProperty(localName = "ConsumerFinance")
        private Long consumerFinance;
    }

    @Data
    public static class TimeWindowStats {
        /**
         * 近3个月指标
         */
        @JacksonXmlProperty(localName = "Last3m")
        private TimeWindowDetail last3m;

        /**
         * 近6个月指标
         */
        @JacksonXmlProperty(localName = "Last6m")
        private TimeWindowDetail last6m;

        /**
         * 近12个月指标
         */
        @JacksonXmlProperty(localName = "Last12m")
        private TimeWindowDetail last12m;
    }

    @Data
    public static class TimeWindowDetail {
        /**
         * 信用卡额度统计
         */
        @JacksonXmlProperty(localName = "CreditCard")
        private LimitStats creditCard;

        /**
         * 循环贷额度统计
         */
        @JacksonXmlProperty(localName = "Revolving")
        private LimitStats revolving;

        /**
         * 消费金融额度统计
         */
        @JacksonXmlProperty(localName = "ConsumerFinance")
        private LimitStats consumerFinance;
    }

    @Data
    public static class LimitStats {
        /**
         * 期间最低额度
         */
        @JacksonXmlProperty(localName = "Min")
        private Long min;

        /**
         * 期间最高额度
         */
        @JacksonXmlProperty(localName = "Max")
        private Long max;

        /**
         * 期间平均额度
         */
        @JacksonXmlProperty(localName = "Avg")
        private Long avg;
    }
}
