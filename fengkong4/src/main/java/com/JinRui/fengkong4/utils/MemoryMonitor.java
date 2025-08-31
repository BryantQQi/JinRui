package com.JinRui.fengkong4.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 内存监控工具类
 * 
 * @author JinRui
 */
@Slf4j
public class MemoryMonitor {

    /**
     * 内存信息数据类
     */
    @Data
    public static class MemoryInfo {
        private long totalMemory;      // 总内存（字节）
        private long freeMemory;       // 空闲内存（字节）
        private long usedMemory;       // 已用内存（字节）
        private long maxMemory;        // 最大内存（字节）
        private double usagePercent;   // 内存使用百分比
        private long totalMemoryMB;    // 总内存（MB）
        private long freeMemoryMB;     // 空闲内存（MB）
        private long usedMemoryMB;     // 已用内存（MB）
        private long maxMemoryMB;      // 最大内存（MB）
    }

    /**
     * 获取当前内存使用信息
     * 
     * @return 内存信息对象
     */
    public static MemoryInfo getCurrentMemoryInfo() {
        Runtime runtime = Runtime.getRuntime();
        
        MemoryInfo memoryInfo = new MemoryInfo();
        memoryInfo.setTotalMemory(runtime.totalMemory());
        memoryInfo.setFreeMemory(runtime.freeMemory());
        memoryInfo.setMaxMemory(runtime.maxMemory());
        memoryInfo.setUsedMemory(memoryInfo.getTotalMemory() - memoryInfo.getFreeMemory());
        memoryInfo.setUsagePercent((double) memoryInfo.getUsedMemory() / memoryInfo.getMaxMemory() * 100);
        
        // 转换为MB
        memoryInfo.setTotalMemoryMB(memoryInfo.getTotalMemory() / 1024 / 1024);
        memoryInfo.setFreeMemoryMB(memoryInfo.getFreeMemory() / 1024 / 1024);
        memoryInfo.setUsedMemoryMB(memoryInfo.getUsedMemory() / 1024 / 1024);
        memoryInfo.setMaxMemoryMB(memoryInfo.getMaxMemory() / 1024 / 1024);
        
        return memoryInfo;
    }

    /**
     * 检查内存使用率是否过高
     * 
     * @param thresholdPercent 阈值百分比
     * @return true表示内存使用率过高
     */
    public static boolean isMemoryUsageHigh(int thresholdPercent) {
        MemoryInfo memoryInfo = getCurrentMemoryInfo();
        return memoryInfo.getUsagePercent() > thresholdPercent;
    }

    /**
     * 记录当前内存使用情况
     * 
     * @param operation 操作描述
     */
    public static void logMemoryUsage(String operation) {
        MemoryInfo memoryInfo = getCurrentMemoryInfo();
        log.info("内存使用情况 [{}] - 总内存: {}MB, 已用: {}MB, 空闲: {}MB, 最大: {}MB, 使用率: {:.2f}%",
                operation,
                memoryInfo.getTotalMemoryMB(),
                memoryInfo.getUsedMemoryMB(),
                memoryInfo.getFreeMemoryMB(),
                memoryInfo.getMaxMemoryMB(),
                memoryInfo.getUsagePercent());
    }

    /**
     * 记录详细的内存使用情况
     * 
     * @param operation 操作描述
     * @param additionalInfo 额外信息
     */
    public static void logDetailedMemoryUsage(String operation, String additionalInfo) {
        MemoryInfo memoryInfo = getCurrentMemoryInfo();
        log.debug("详细内存使用情况 [{}] - 总内存: {}MB({}字节), 已用: {}MB({}字节), " +
                 "空闲: {}MB({}字节), 最大: {}MB({}字节), 使用率: {:.2f}% - {}",
                operation,
                memoryInfo.getTotalMemoryMB(), memoryInfo.getTotalMemory(),
                memoryInfo.getUsedMemoryMB(), memoryInfo.getUsedMemory(),
                memoryInfo.getFreeMemoryMB(), memoryInfo.getFreeMemory(),
                memoryInfo.getMaxMemoryMB(), memoryInfo.getMaxMemory(),
                memoryInfo.getUsagePercent(),
                additionalInfo);
    }

    /**
     * 强制执行垃圾回收
     */
    public static void forceGC() {
        log.debug("执行强制垃圾回收...");
        long beforeMemory = getCurrentMemoryInfo().getUsedMemory();
        
        System.gc();
        
        // 等待一小段时间让GC完成
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long afterMemory = getCurrentMemoryInfo().getUsedMemory();
        long freedMemory = beforeMemory - afterMemory;
        
        log.debug("垃圾回收完成 - 释放内存: {}MB", freedMemory / 1024 / 1024);
    }

    /**
     * 检查内存使用并在必要时发出警告
     * 
     * @param thresholdPercent 警告阈值
     * @param operation 当前操作描述
     * @return 内存使用百分比
     */
    public static double checkMemoryAndWarn(int thresholdPercent, String operation) {
        MemoryInfo memoryInfo = getCurrentMemoryInfo();
        double usagePercent = memoryInfo.getUsagePercent();
        
        if (usagePercent > thresholdPercent) {
            log.warn("⚠️ 内存使用率过高 [{}] - 当前使用率: {:.2f}%, 阈值: {}%, " +
                    "建议执行垃圾回收或减少批次大小",
                    operation, usagePercent, thresholdPercent);
        }
        
        return usagePercent;
    }

    /**
     * 计算建议的批次大小
     * 
     * @param currentBatchSize 当前批次大小
     * @param memoryUsagePercent 内存使用百分比
     * @param maxMemoryThreshold 最大内存阈值
     * @param minBatchSize 最小批次大小
     * @param maxBatchSize 最大批次大小
     * @return 建议的批次大小
     */
    public static int calculateRecommendedBatchSize(int currentBatchSize, double memoryUsagePercent,
                                                   int maxMemoryThreshold, int minBatchSize, int maxBatchSize) {
        if (memoryUsagePercent < maxMemoryThreshold * 0.6) {
            // 内存使用率低，可以增加批次大小
            int newSize = (int) (currentBatchSize * 1.2);
            return Math.min(newSize, maxBatchSize);
        } else if (memoryUsagePercent > maxMemoryThreshold) {
            // 内存使用率过高，需要减少批次大小
            int newSize = (int) (currentBatchSize * 0.7);
            return Math.max(newSize, minBatchSize);
        } else {
            // 内存使用率适中，保持当前批次大小
            return currentBatchSize;
        }
    }

    /**
     * 获取内存使用统计信息的字符串表示
     * 
     * @return 内存统计字符串
     */
    public static String getMemoryStatsString() {
        MemoryInfo memoryInfo = getCurrentMemoryInfo();
        return String.format("内存统计: 已用%dMB/最大%dMB (%.2f%%)",
                memoryInfo.getUsedMemoryMB(),
                memoryInfo.getMaxMemoryMB(),
                memoryInfo.getUsagePercent());
    }

    /**
     * 检查是否有足够的内存来处理指定大小的批次
     * 
     * @param estimatedBatchMemoryMB 预估批次内存占用（MB）
     * @param safetyMarginPercent 安全边际百分比
     * @return true表示内存足够
     */
    public static boolean hasEnoughMemoryForBatch(long estimatedBatchMemoryMB, int safetyMarginPercent) {
        MemoryInfo memoryInfo = getCurrentMemoryInfo();
        long availableMemory = memoryInfo.getMaxMemoryMB() - memoryInfo.getUsedMemoryMB();
        long requiredMemory = estimatedBatchMemoryMB * (100 + safetyMarginPercent) / 100;
        
        boolean hasEnough = availableMemory >= requiredMemory;
        
        if (!hasEnough) {
            log.warn("内存不足警告 - 可用内存: {}MB, 需要内存: {}MB (包含{}%安全边际)",
                    availableMemory, requiredMemory, safetyMarginPercent);
        }
        
        return hasEnough;
    }
}
