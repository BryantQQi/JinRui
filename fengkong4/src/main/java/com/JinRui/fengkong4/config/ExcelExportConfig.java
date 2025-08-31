package com.JinRui.fengkong4.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Excel导出配置类
 * 
 * @author JinRui
 */
@Data
@Component
@ConfigurationProperties(prefix = "excel.export")
public class ExcelExportConfig {

    /**
     * 分批处理的批次大小
     * 默认5000条记录每批
     */
    private int batchSize = 1000;

    /**
     * 内存使用警告阈值（百分比）
     * 当内存使用超过此值时记录警告日志
     */
    private int maxMemoryUsagePercent = 80;

    /**
     * 是否启用进度日志记录
     */
    private boolean enableProgressLogging = true;

    /**
     * 进度报告间隔（批次数）
     * 每处理多少批次记录一次进度日志
     */
    private int progressReportInterval = 10;

    /**
     * 是否在每批处理后强制垃圾回收
     */
    private boolean enableForceGC = true;

    /**
     * 内存使用率超过阈值时是否自动调整批次大小
     */
    private boolean enableAdaptiveBatchSize = true;

    /**
     * 自适应批次大小的最小值
     */
    private int minBatchSize = 1000;

    /**
     * 自适应批次大小的最大值
     */
    private int maxBatchSize = 10000;

    /**
     * 导出超时时间（分钟）
     */
    private int exportTimeoutMinutes = 30;

    /**
     * 是否启用详细的内存监控日志
     */
    private boolean enableDetailedMemoryLogging = false;
}
