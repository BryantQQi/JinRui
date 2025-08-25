package com.JinRui.fengkong4.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 征信报文配置类
 * 
 * @author JinRui
 */
@Data
@Component
@ConfigurationProperties(prefix = "credit-report")
public class CreditReportConfig {

    /**
     * 报文版本
     */
    private String version = "2.0";

    /**
     * 机构代码
     */
    private String institutionCode = "FENGKONG001";

    /**
     * 数据生成配置
     */
    private DataGeneration dataGeneration = new DataGeneration();

    @Data
    public static class DataGeneration {
        /**
         * 是否启用随机数据生成
         */
        private boolean enableRandom = true;

        /**
         * 默认生成的信贷记录数量
         */
        private int defaultCreditRecords = 5;

        /**
         * 默认生成的查询记录数量
         */
        private int defaultQueryRecords = 3;
    }
}
