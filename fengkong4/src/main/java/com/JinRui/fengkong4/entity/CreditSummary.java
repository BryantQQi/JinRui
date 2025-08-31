package com.JinRui.fengkong4.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * 信用概要增强
 * 
 * @author JinRui
 */
@Data
public class CreditSummary {

    /**
     * 特殊记录标记（影响风控决策）
     */
    @JacksonXmlProperty(localName = "SpecialRecords")
    private SpecialRecords specialRecords;

    /**
     * 贷款逾期统计（过去5年内）
     */
    @JacksonXmlProperty(localName = "LoanOverdue")
    private OverdueStats loanOverdue;

    /**
     * 贷记卡逾期统计
     */
    @JacksonXmlProperty(localName = "CreditCardOverdue")
    private OverdueStats creditCardOverdue;

    /**
     * 异常状态标记
     */
    @JacksonXmlProperty(localName = "AbnormalFlags")
    private AbnormalFlags abnormalFlags;

    @Data
    public static class SpecialRecords {
        /**
         * 是否出现过呆账（true直接拒贷）
         */
        @JacksonXmlProperty(localName = "HasBadDebt")
        private Boolean hasBadDebt;

        /**
         * 是否出现过资产处置
         */
        @JacksonXmlProperty(localName = "HasAssetDisposal")
        private Boolean hasAssetDisposal;

        /**
         * 是否出现过保证人代偿（反映还款能力问题）
         */
        @JacksonXmlProperty(localName = "HasGuarantorRepayment")
        private Boolean hasGuarantorRepayment;
    }

    @Data
    public static class OverdueStats {
        /**
         * 发生过逾期的账户数量
         */
        @JacksonXmlProperty(localName = "Count")
        private Integer count;

        /**
         * 所有账户累计逾期月份总数
         */
        @JacksonXmlProperty(localName = "Months")
        private Integer months;

        /**
         * 单月最高逾期金额（单位：元）
         */
        @JacksonXmlProperty(localName = "MaxAmount")
        private Long maxAmount;

        /**
         * 最长连续逾期月数
         */
        @JacksonXmlProperty(localName = "MaxDuration")
        private Integer maxDuration;
    }

    @Data
    public static class AbnormalFlags {
        /**
         * 信用卡是否存在呆账/止付状态
         */
        @JacksonXmlProperty(localName = "HasCreditCardAbnormal")
        private Boolean hasCreditCardAbnormal;

        /**
         * 贷款是否出现异常五级分类（如次级、可疑、损失）
         */
        @JacksonXmlProperty(localName = "HasAbnormalLoanClass")
        private Boolean hasAbnormalLoanClass;
    }
}
