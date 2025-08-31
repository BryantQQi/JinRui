package com.JinRui.fengkong4.controller;

import com.JinRui.fengkong4.common.ApiResult;
import com.JinRui.fengkong4.entity.CreditReport;
import com.JinRui.fengkong4.service.CreditDataStorageService;
import com.JinRui.fengkong4.service.CreditReportService;
import com.JinRui.fengkong4.service.CreditReportExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 征信报告批量数据生成控制器
 * 用于生成大量测试数据以模拟OOM场景
 * 
 * @author JinRui
 */
@Slf4j
@RestController
@RequestMapping("/api/credit-report/data")
public class CreditReportDataController {

    @Autowired
    private CreditReportService creditReportService;
    
    @Autowired
    private CreditDataStorageService creditDataStorageService;
    
    @Autowired
    private CreditReportExcelService creditReportExcelService;

    // 线程池用于异步批量生成数据
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    /**
     * 批量生成征信报告数据
     * 
     * @param count 生成数量
     * @param async 是否异步生成
     * @return 生成结果
     */
    @PostMapping("/generate/batch")
    public ApiResult<Map<String, Object>> generateBatchCreditReports(
            @RequestParam(value = "count", defaultValue = "1000") int count,
            @RequestParam(value = "async", defaultValue = "false") boolean async) {
        
        log.info("接收到批量生成征信报告数据请求，数量：{}，异步模式：{}", count, async);
        
        try {
            // 参数验证
            if (count <= 0) {
                return ApiResult.fail(400, "生成数量必须大于0");
            }
            
            if (count > 200000) {
                return ApiResult.fail(400, "单次生成数量不能超过20万条，请分批生成");
            }
            
            // 获取当前数据量
            long currentCount = creditReportExcelService.getCreditReportCount();
            log.info("当前数据库中已有{}条征信报告数据", currentCount);
            
            Map<String, Object> result = new HashMap<>();
            result.put("requestCount", count);
            result.put("currentCount", currentCount);
            result.put("async", async);
            
            if (async) {
                // 异步生成
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                    return generateCreditReportsSync(count);
                }, executorService);
                
                result.put("status", "started");
                result.put("message", "批量数据生成已开始，正在后台处理...");
                
                // 异步处理完成后记录日志
                future.whenComplete((generateResult, throwable) -> {
                    if (throwable != null) {
                        log.error("异步批量生成征信报告数据失败", throwable);
                    } else {
                        log.info("异步批量生成征信报告数据完成：{}", generateResult);
                    }
                });
                
            } else {
                // 同步生成
                long startTime = System.currentTimeMillis();
                String generateResult = generateCreditReportsSync(count);
                long endTime = System.currentTimeMillis();
                
                result.put("status", "completed");
                result.put("message", generateResult);
                result.put("duration", endTime - startTime);
                result.put("finalCount", creditReportExcelService.getCreditReportCount());
            }
            
            return ApiResult.ok(result);
            
        } catch (Exception e) {
            log.error("批量生成征信报告数据失败", e);
            return ApiResult.fail(500, "批量生成失败：" + e.getMessage());
        }
    }

    /**
     * 快速生成OOM测试数据
     * 生成足够多的数据用于触发OOM
     */
    @PostMapping("/generate/oom-test")
    public ApiResult<Map<String, Object>> generateOOMTestData(
            @RequestParam(value = "targetCount", defaultValue = "100000") int targetCount) {
        
        log.info("接收到OOM测试数据生成请求，目标数量：{}", targetCount);
        
        try {
            // 获取当前数据量
            long currentCount = creditReportExcelService.getCreditReportCount();
            log.info("当前数据库中已有{}条征信报告数据", currentCount);
            
            if (currentCount >= targetCount) {
                Map<String, Object> result = new HashMap<>();
                result.put("message", "数据库中已有足够的数据用于OOM测试");
                result.put("currentCount", currentCount);
                result.put("targetCount", targetCount);
                return ApiResult.ok(result);
            }
            
            int needGenerate = (int) (targetCount - currentCount);
            log.info("需要生成{}条数据以达到目标数量", needGenerate);
            
            // 异步生成数据
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                return generateCreditReportsSync(needGenerate);
            }, executorService);
            
            Map<String, Object> result = new HashMap<>();
            result.put("status", "started");
            result.put("message", "OOM测试数据生成已开始，正在后台处理...");
            result.put("currentCount", currentCount);
            result.put("targetCount", targetCount);
            result.put("needGenerate", needGenerate);
            result.put("estimatedTimeMinutes", needGenerate / 100); // 预估每秒生成100条
            
            // 异步处理完成后记录日志
            future.whenComplete((generateResult, throwable) -> {
                if (throwable != null) {
                    log.error("OOM测试数据生成失败", throwable);
                } else {
                    log.info("OOM测试数据生成完成：{}，最终数据量：{}", 
                            generateResult, creditReportExcelService.getCreditReportCount());
                }
            });
            
            return ApiResult.ok(result);
            
        } catch (Exception e) {
            log.error("OOM测试数据生成失败", e);
            return ApiResult.fail(500, "OOM测试数据生成失败：" + e.getMessage());
        }
    }

    /**
     * 清空所有征信报告数据
     */
    @DeleteMapping("/clear/all")
    public ApiResult<Map<String, Object>> clearAllCreditReports() {
        log.info("接收到清空所有征信报告数据的请求");
        
        try {
            long beforeCount = creditReportExcelService.getCreditReportCount();
            log.info("清空前数据量：{}", beforeCount);
            
            if (beforeCount == 0) {
                Map<String, Object> result = new HashMap<>();
                result.put("message", "数据库中没有征信报告数据需要清空");
                result.put("beforeCount", 0);
                result.put("afterCount", 0);
                return ApiResult.ok(result);
            }
            
            // 这里应该调用repository的deleteAll方法，但为了安全起见，我们先记录日志
            log.warn("⚠️ 准备清空{}条征信报告数据，这是一个危险操作！", beforeCount);
            
            // TODO: 实际实现时需要调用 creditReportRepository.deleteAll();
            // 目前先返回提示信息
            
            Map<String, Object> result = new HashMap<>();
            result.put("message", "清空操作已记录，请联系管理员确认执行");
            result.put("beforeCount", beforeCount);
            result.put("warning", "这是一个危险操作，会删除所有征信报告数据");
            
            return ApiResult.ok(result);
            
        } catch (Exception e) {
            log.error("清空征信报告数据失败", e);
            return ApiResult.fail(500, "清空数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取数据生成进度和状态
     */
    @GetMapping("/status")
    public ApiResult<Map<String, Object>> getDataGenerationStatus() {
        try {
            Map<String, Object> status = new HashMap<>();
            
            // 数据量统计
            long totalCount = creditReportExcelService.getCreditReportCount();
            status.put("totalCount", totalCount);
            
            // 内存状态
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
            memoryInfo.put("usagePercent", String.format("%.2f%%", (double) usedMemory / maxMemory * 100));
            
            status.put("memoryInfo", memoryInfo);
            
            // OOM风险评估
            String riskLevel;
            if (totalCount < 10000) {
                riskLevel = "低风险";
            } else if (totalCount < 50000) {
                riskLevel = "中等风险";
            } else if (totalCount < 100000) {
                riskLevel = "高风险";
            } else {
                riskLevel = "极高风险";
            }
            
            status.put("oomRiskLevel", riskLevel);
            status.put("readyForOOMTest", totalCount >= 50000);
            
            return ApiResult.ok(status);
            
        } catch (Exception e) {
            log.error("获取数据生成状态失败", e);
            return ApiResult.fail(500, "获取状态失败：" + e.getMessage());
        }
    }

    /**
     * 同步生成征信报告数据的核心方法
     */
    private String generateCreditReportsSync(int count) {
        log.info("开始同步生成{}条征信报告数据", count);
        
        long startTime = System.currentTimeMillis();
        int successCount = 0;
        int failCount = 0;
        
        try {
            // 分批生成，每批1000条
            int batchSize = 1000;
            int batches = (count + batchSize - 1) / batchSize;
            
            for (int batch = 0; batch < batches; batch++) {
                int currentBatchSize = Math.min(batchSize, count - batch * batchSize);
                
                log.info("正在生成第{}/{}批数据，本批数量：{}", batch + 1, batches, currentBatchSize);
                
                for (int i = 0; i < currentBatchSize; i++) {
                    try {
                        // 生成征信报文
                        CreditReport report = creditReportService.generateRandomReport();
                        
                        // 存储到数据库
                        creditDataStorageService.saveCreditScoreResult(
                            report.getPersonalInfo().getIdNumber(),
                            report.getPersonalInfo().getName(),
                            (int) (Math.random() * 800 + 300), // 随机评分300-800
                            report,
                            (long) (Math.random() * 1000 + 100) // 随机AI响应时间
                        );
                        
                        successCount++;
                        
                        // 每100条记录一次进度
                        if ((successCount + failCount) % 100 == 0) {
                            log.debug("已处理{}条数据，成功{}条，失败{}条", 
                                    successCount + failCount, successCount, failCount);
                        }
                        
                    } catch (Exception e) {
                        failCount++;
                        log.warn("生成第{}条征信报告数据失败：{}", successCount + failCount, e.getMessage());
                    }
                }
                
                // 每批之间稍微休息，避免过度占用资源
                if (batch < batches - 1) {
                    Thread.sleep(100);
                }
            }
            
        } catch (Exception e) {
            log.error("批量生成征信报告数据过程中发生异常", e);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        String result = String.format("批量生成完成：成功%d条，失败%d条，总耗时%dms", 
                                     successCount, failCount, duration);
        
        log.info(result);
        return result;
    }
}
