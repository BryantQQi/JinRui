package com.JinRui.fengkong4.controller;

import com.JinRui.fengkong4.common.ApiResult;
import com.JinRui.fengkong4.service.CreditReportExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 征信报告Excel导出控制器
 * 
 * @author JinRui
 */
@Slf4j
@RestController
@RequestMapping("/api/credit-report/excel")
public class CreditReportExcelController {

    @Autowired
    private CreditReportExcelService creditReportExcelService;

    /**
     * 导出所有征信报告数据到Excel
     * ⚠️ 警告：此接口会一次性查询所有数据到内存，可能导致OOM！
     */
    @GetMapping("/export/all")
    public void exportAllCreditReports(HttpServletResponse response) {
        log.info("接收到导出所有征信报告Excel的请求");
        
        try {
            // 记录开始时间和内存状态
            long startTime = System.currentTimeMillis();
            Runtime runtime = Runtime.getRuntime();
            long beforeMemory = runtime.totalMemory() - runtime.freeMemory();
            
            log.info("开始导出Excel - 当前已用内存：{}MB", beforeMemory / 1024 / 1024);
            
            // 获取数据总量
            long totalCount = creditReportExcelService.getCreditReportCount();
            log.info("准备导出{}条征信报告数据", totalCount);
            
            if (totalCount == 0) {
                log.warn("没有征信报告数据可供导出");
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                response.getWriter().write("没有征信报告数据可供导出");
                return;
            }
            
            // 大数据量警告
            if (totalCount > 50000) {
                log.warn("⚠️ 数据量较大（{}条），可能导致内存溢出！", totalCount);
            }
            
            // 执行导出
            creditReportExcelService.exportAllCreditReports(response);
            
            long endTime = System.currentTimeMillis();
            long afterMemory = runtime.totalMemory() - runtime.freeMemory();
            
            log.info("Excel导出完成 - 总耗时：{}ms，内存变化：{}MB -> {}MB", 
                    endTime - startTime, 
                    beforeMemory / 1024 / 1024, 
                    afterMemory / 1024 / 1024);
            
        } catch (OutOfMemoryError oom) {
            log.error("🔥 发生内存溢出！这正是我们要观察的OOM场景", oom);
            
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write("{\"code\":500,\"msg\":\"内存不足，无法完成Excel导出。数据量过大，请联系管理员处理。\",\"data\":null}");
            } catch (IOException e) {
                log.error("写入OOM错误响应失败", e);
            }
            
        } catch (Exception e) {
            log.error("导出Excel时发生异常", e);
            
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write("{\"code\":500,\"msg\":\"Excel导出失败：" + e.getMessage() + "\",\"data\":null}");
            } catch (IOException ioException) {
                log.error("写入错误响应失败", ioException);
            }
        }
    }

    /**
     * 导出指定数量的征信报告数据到Excel（用于测试不同数据量）
     */
    @GetMapping("/export/limit")
    public void exportCreditReportsWithLimit(
            HttpServletResponse response,
            @RequestParam(value = "limit", defaultValue = "1000") int limit) {
        
        log.info("接收到导出限制数量征信报告Excel的请求，限制数量：{}", limit);
        
        try {
            // 参数验证
            if (limit <= 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("导出数量必须大于0");
                return;
            }
            
            if (limit > 100000) {
                log.warn("⚠️ 导出数量过大（{}条），可能导致内存问题", limit);
            }
            
            // 执行导出
            creditReportExcelService.exportCreditReportsWithLimit(response, limit);
            
            log.info("限制数量Excel导出请求处理完成，导出数量：{}", limit);
            
        } catch (Exception e) {
            log.error("导出限制数量Excel时发生异常", e);
            
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write("{\"code\":500,\"msg\":\"Excel导出失败：" + e.getMessage() + "\",\"data\":null}");
            } catch (IOException ioException) {
                log.error("写入错误响应失败", ioException);
            }
        }
    }

    /**
     * 获取征信报告数据统计信息
     */
    @GetMapping("/stats")
    public ApiResult<Map<String, Object>> getCreditReportStats() {
        log.info("接收到获取征信报告统计信息的请求");
        
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // 获取总数量
            long totalCount = creditReportExcelService.getCreditReportCount();
            stats.put("totalCount", totalCount);
            
            // 内存信息
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            long maxMemory = runtime.maxMemory();
            
            Map<String, Object> memoryInfo = new HashMap<>();
            memoryInfo.put("totalMemoryMB", totalMemory / 1024 / 1024);
            memoryInfo.put("usedMemoryMB", usedMemory / 1024 / 1024);
            memoryInfo.put("freeMemoryMB", freeMemory / 1024 / 1024);
            memoryInfo.put("maxMemoryMB", maxMemory / 1024 / 1024);
            memoryInfo.put("memoryUsagePercent", (double) usedMemory / maxMemory * 100);
            
            stats.put("memoryInfo", memoryInfo);
            
            // 预估导出风险
            String riskLevel;
            String riskMessage;
            if (totalCount == 0) {
                riskLevel = "无数据";
                riskMessage = "没有征信报告数据";
            } else if (totalCount < 1000) {
                riskLevel = "安全";
                riskMessage = "数据量较小，导出安全";
            } else if (totalCount < 10000) {
                riskLevel = "注意";
                riskMessage = "数据量适中，建议监控内存使用";
            } else if (totalCount < 50000) {
                riskLevel = "警告";
                riskMessage = "数据量较大，可能导致内存压力";
            } else {
                riskLevel = "危险";
                riskMessage = "数据量很大，极可能导致OOM！";
            }
            
            stats.put("exportRisk", riskLevel);
            stats.put("riskMessage", riskMessage);
            
            // 建议的导出策略
            String suggestion;
            if (totalCount < 1000) {
                suggestion = "可以直接使用 /export/all 接口导出";
            } else if (totalCount < 10000) {
                suggestion = "建议使用 /export/limit?limit=5000 分批导出";
            } else {
                suggestion = "强烈建议分批导出，或联系管理员处理大数据量导出";
            }
            
            stats.put("suggestion", suggestion);
            
            log.info("征信报告统计信息查询完成：总数量={}，风险等级={}，内存使用率={:.2f}%", 
                    totalCount, riskLevel, (double) usedMemory / maxMemory * 100);
            
            return ApiResult.ok(stats);
            
        } catch (Exception e) {
            log.error("获取征信报告统计信息失败", e);
            return ApiResult.fail(500, "获取统计信息失败：" + e.getMessage());
        }
    }

    /**
     * 优化版本：分批导出所有征信报告数据到Excel
     * 🚀 使用分批查询和流式写入避免OOM
     */
    @GetMapping("/export/all-optimized")
    public void exportAllCreditReportsOptimized(HttpServletResponse response) {
        log.info("接收到优化版本导出所有征信报告Excel的请求");
        
        try {
            // 记录开始时间和内存状态
            long startTime = System.currentTimeMillis();
            Runtime runtime = Runtime.getRuntime();
            long beforeMemory = runtime.totalMemory() - runtime.freeMemory();
            
            log.info("开始优化版Excel导出 - 当前已用内存：{}MB", beforeMemory / 1024 / 1024);
            
            // 获取数据总量
            long totalCount = creditReportExcelService.getCreditReportCount();
            log.info("准备优化导出{}条征信报告数据", totalCount);
            
            if (totalCount == 0) {
                log.warn("没有征信报告数据可供导出");
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                response.getWriter().write("没有征信报告数据可供导出");
                return;
            }
            
            // 执行优化版导出
            creditReportExcelService.exportAllCreditReportsOptimized(response);
            
            long endTime = System.currentTimeMillis();
            long afterMemory = runtime.totalMemory() - runtime.freeMemory();
            
            log.info("优化版Excel导出完成 - 总耗时：{}ms，内存变化：{}MB -> {}MB", 
                    endTime - startTime, 
                    beforeMemory / 1024 / 1024, 
                    afterMemory / 1024 / 1024);
            
        } catch (Exception e) {
            log.error("优化版Excel导出失败", e);
            
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write("{\"code\":500,\"msg\":\"优化版Excel导出失败：" + e.getMessage() + "\",\"data\":null}");
            } catch (IOException ioException) {
                log.error("写入错误响应失败", ioException);
            }
        }
    }

    /**
     * 带详细进度反馈的优化版导出
     */
    @GetMapping("/export/all-with-progress")
    public void exportAllCreditReportsWithProgress(HttpServletResponse response) {
        log.info("接收到带进度反馈的优化版本导出请求");
        
        try {
            // 创建详细的进度回调
            DetailedExportProgressCallback callback = new DetailedExportProgressCallback();
            
            // 执行带回调的导出
            creditReportExcelService.exportAllCreditReportsWithCallback(response, callback);
            
            // 输出最终统计信息
            log.info("带进度反馈的导出完成 - {}", callback.getFinalSummary());
            
        } catch (Exception e) {
            log.error("带进度反馈的Excel导出失败", e);
            
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write("{\"code\":500,\"msg\":\"Excel导出失败：" + e.getMessage() + "\",\"data\":null}");
            } catch (IOException ioException) {
                log.error("写入错误响应失败", ioException);
            }
        }
    }

    /**
     * 获取导出性能对比信息
     */
    @GetMapping("/performance/comparison")
    public ApiResult<Map<String, Object>> getPerformanceComparison() {
        try {
            Map<String, Object> comparison = new HashMap<>();
            
            // 获取数据统计
            long totalCount = creditReportExcelService.getCreditReportCount();
            
            // 内存信息
            Runtime runtime = Runtime.getRuntime();
            long maxMemoryMB = runtime.maxMemory() / 1024 / 1024;
            long currentMemoryMB = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
            
            // 预估性能对比
            Map<String, Object> originalMethod = new HashMap<>();
            originalMethod.put("name", "原始方法（一次性查询）");
            originalMethod.put("memoryRequired", Math.max(totalCount * 2 / 1024, 100) + "MB"); // 预估内存需求
            originalMethod.put("oomRisk", totalCount > 50000 ? "极高" : totalCount > 10000 ? "高" : "中");
            originalMethod.put("description", "一次性查询所有数据到内存，然后导出");
            
            Map<String, Object> optimizedMethod = new HashMap<>();
            optimizedMethod.put("name", "优化方法（分批处理）");
            optimizedMethod.put("memoryRequired", "50-100MB"); // 固定较小的内存需求
            optimizedMethod.put("oomRisk", "低");
            optimizedMethod.put("description", "分批查询数据，流式写入Excel，内存使用可控");
            
            comparison.put("totalRecords", totalCount);
            comparison.put("currentMemoryUsage", currentMemoryMB + "MB");
            comparison.put("maxAvailableMemory", maxMemoryMB + "MB");
            comparison.put("originalMethod", originalMethod);
            comparison.put("optimizedMethod", optimizedMethod);
            comparison.put("recommendation", totalCount > 10000 ? "强烈建议使用优化方法" : "两种方法都可以使用");
            
            return ApiResult.ok(comparison);
            
        } catch (Exception e) {
            log.error("获取性能对比信息失败", e);
            return ApiResult.fail(500, "获取性能对比信息失败：" + e.getMessage());
        }
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public ApiResult<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "credit-report-excel-export");
        response.put("version", "2.0.0"); // 升级版本号
        response.put("description", "征信报告Excel导出服务（含优化版本）");
        
        // 添加内存状态
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        long maxMemory = runtime.maxMemory();
        double memoryUsage = (double) usedMemory / maxMemory * 100;
        
        response.put("memoryUsagePercent", String.format("%.2f%%", memoryUsage));
        response.put("availableMethods", new String[]{
            "/export/all - 原始方法（可能OOM）",
            "/export/all-optimized - 优化方法（避免OOM）",
            "/export/all-with-progress - 带进度反馈的优化方法"
        });
        
        return ApiResult.ok(response);
    }

    /**
     * 详细的导出进度回调实现
     */
    private static class DetailedExportProgressCallback implements com.JinRui.fengkong4.callback.ExportProgressCallback {
        
        private long startTime = System.currentTimeMillis();
        private int totalBatches = 0;
        private long totalRecords = 0;
        
        @Override
        public void onProgress(int currentBatch, int totalBatches, long processedCount, long totalCount) {
            this.totalBatches = totalBatches;
            this.totalRecords = totalCount;
            
            double progress = (double) processedCount / totalCount * 100;
            long elapsed = System.currentTimeMillis() - startTime;
            long estimatedTotal = processedCount > 0 ? elapsed * totalCount / processedCount : 0;
            long remaining = estimatedTotal - elapsed;
            
            log.info("📊 导出进度: {:.1f}% ({}/{}), 批次: {}/{}, 已用时间: {}ms, 预计剩余: {}ms",
                    progress, processedCount, totalCount, currentBatch, totalBatches,
                    elapsed, remaining);
        }

        @Override
        public void onBatchCompleted(int batchNumber, int batchSize, int processedCount, long batchTime, 
                                   long memoryUsed, long memoryTotal) {
            double memoryPercent = (double) memoryUsed / memoryTotal * 100;
            log.debug("✅ 批次 {} 完成: 处理{}条记录, 耗时{}ms, 内存使用{:.1f}%",
                    batchNumber, processedCount, batchTime, memoryPercent);
        }

        @Override
        public void onError(Exception e, int currentBatch, long processedCount) {
            log.error("❌ 批次 {} 处理失败: 已处理{}条记录, 错误: {}", 
                    currentBatch, processedCount, e.getMessage());
        }

        @Override
        public void onCompleted(long totalProcessed, long totalTime, int successBatches, int failedBatches) {
            double avgBatchTime = successBatches > 0 ? (double) totalTime / successBatches : 0;
            double throughput = totalTime > 0 ? (double) totalProcessed * 1000 / totalTime : 0;
            
            log.info("🎉 导出完成! 总记录: {}, 总耗时: {}ms, 成功批次: {}, 失败批次: {}, " +
                    "平均批次耗时: {:.1f}ms, 吞吐量: {:.1f}条/秒",
                    totalProcessed, totalTime, successBatches, failedBatches,
                    avgBatchTime, throughput);
        }

        @Override
        public void onMemoryWarning(double memoryUsagePercent, int currentBatch, String recommendedAction) {
            log.warn("⚠️ 内存警告: 批次{}, 内存使用率{:.1f}%, 建议: {}",
                    currentBatch, memoryUsagePercent, recommendedAction);
        }

        @Override
        public void onBatchSizeAdjusted(int oldBatchSize, int newBatchSize, String reason) {
            log.info("🔧 批次大小调整: {} -> {}, 原因: {}", oldBatchSize, newBatchSize, reason);
        }
        
        public String getFinalSummary() {
            long totalTime = System.currentTimeMillis() - startTime;
            return String.format("总批次: %d, 总记录: %d, 总耗时: %dms", 
                    totalBatches, totalRecords, totalTime);
        }
    }
}
