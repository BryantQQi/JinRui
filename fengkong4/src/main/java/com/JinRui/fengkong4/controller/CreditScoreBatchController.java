package com.JinRui.fengkong4.controller;

import com.JinRui.fengkong4.common.ApiResult;
import com.JinRui.fengkong4.entity.CreditScoreRequest;
import com.JinRui.fengkong4.service.CreditScoreProducerService;
import com.JinRui.fengkong4.service.UserInfoGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 征信评分批量处理控制器
 * 
 * @author JinRui
 */
@Slf4j
@RestController
@RequestMapping("/api/credit/batch")
public class CreditScoreBatchController {

    @Autowired
    private UserInfoGeneratorService userInfoGeneratorService;

    @Autowired
    private CreditScoreProducerService creditScoreProducerService;

    /**
     * 批量征信评分接口
     * 
     * @param count 生成数量，默认100个
     * @return 处理结果
     */
    @PostMapping("/score")
    public ApiResult<Map<String, Object>> batchCreditScore(
            @RequestParam(value = "count", defaultValue = "100") Integer count) {
        
        long startTime = System.currentTimeMillis();
        String batchId = UUID.randomUUID().toString();
        
        try {
            log.info("接收到批量征信评分请求，数量：{}，批次ID：{}", count, batchId);
            
            // 参数验证
            if (count <= 0 || count > 1000) {
                return ApiResult.fail(400, "请求数量必须在1-1000之间");
            }
            
            // 1. 生成用户信息
            log.info("开始生成用户信息，数量：{}", count);
            List<CreditScoreRequest> requests = userInfoGeneratorService.generateUserInfoBatch(count, batchId);
            
            // 2. 发送到消息队列
            log.info("开始发送消息到队列，数量：{}", requests.size());
            creditScoreProducerService.sendBatchCreditScoreRequests(requests);
            
            // 3. 构建响应结果
            Map<String, Object> result = new HashMap<>();
            result.put("batchId", batchId);
            result.put("requestCount", requests.size());
            result.put("status", "SUBMITTED");
            result.put("message", "批量征信评分请求已提交，正在后台处理");
            result.put("estimatedProcessTime", requests.size() * 2 + " 秒");
            
            long endTime = System.currentTimeMillis();
            result.put("submitTime", endTime - startTime + "ms");
            
            log.info("批量征信评分请求提交成功，批次ID：{}，数量：{}，耗时：{}ms", 
                    batchId, requests.size(), endTime - startTime);
            
            return ApiResult.ok(result);
            
        } catch (Exception e) {
            log.error("批量征信评分请求处理失败，批次ID：{}，错误：{}", batchId, e.getMessage(), e);
            
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("batchId", batchId);
            errorResult.put("status", "FAILED");
            errorResult.put("error", e.getMessage());
            
            return ApiResult.fail(500, "批量征信评分请求处理失败", errorResult);
        }
    }

    /**
     * 测试接口 - 发送少量征信评分请求
     * 
     * @return 处理结果
     */
    @GetMapping("/test")
    public ApiResult<Map<String, Object>> testBatchCreditScore() {
        return batchCreditScore(5); // 测试时只生成5个
    }

    /**
     * 查询批次处理状态接口
     * 
     * @param batchId 批次ID
     * @return 处理状态
     */
    @GetMapping("/status/{batchId}")
    public ApiResult<Map<String, Object>> getBatchStatus(@PathVariable String batchId) {
        try {
            log.info("查询批次处理状态，批次ID：{}", batchId);
            
            // TODO: 实现批次状态查询逻辑
            // 这里暂时返回模拟状态
            Map<String, Object> status = new HashMap<>();
            status.put("batchId", batchId);
            status.put("status", "PROCESSING");
            status.put("message", "批次正在处理中");
            status.put("note", "状态查询功能待完善");
            
            return ApiResult.ok(status);
            
        } catch (Exception e) {
            log.error("查询批次处理状态失败，批次ID：{}，错误：{}", batchId, e.getMessage(), e);
            return ApiResult.fail(500, "查询批次处理状态失败：" + e.getMessage());
        }
    }
}
