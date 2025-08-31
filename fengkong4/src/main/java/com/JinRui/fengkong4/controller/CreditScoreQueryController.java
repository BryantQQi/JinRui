package com.JinRui.fengkong4.controller;

import com.JinRui.fengkong4.common.ApiResult;
import com.JinRui.fengkong4.entity.mongo.CreditReportMongo;
import com.JinRui.fengkong4.service.CreditScoreQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 征信评分查询控制器
 * 
 * @author JinRui
 */
@Slf4j
@RestController
@RequestMapping("/api/credit/query")
public class CreditScoreQueryController {

    @Autowired
    private CreditScoreQueryService creditScoreQueryService;

    /**
     * 根据身份证号码和姓名查询征信评分结果
     * 
     * @param idCard 身份证号码
     * @param name 姓名
     * @return 征信评分结果
     */
    @GetMapping("/score")
    public ApiResult<CreditReportMongo> getCreditScore(
            @RequestParam("idCard") String idCard,
            @RequestParam("name") String name) {
        
        try {
            log.info("接收到征信评分查询请求：身份证={}，姓名={}", 
                    maskIdCard(idCard), name);
            
            // 参数验证
            if (idCard == null || idCard.trim().isEmpty()) {
                return ApiResult.fail(400, "身份证号码不能为空");
            }
            
            if (name == null || name.trim().isEmpty()) {
                return ApiResult.fail(400, "姓名不能为空");
            }
            
            CreditReportMongo result = creditScoreQueryService.getCreditScoreByIdAndName(
                    idCard.trim(), name.trim());
            
            if (result == null) {
                return ApiResult.fail(404, "未找到对应的征信评分记录");
            }
            
            log.info("征信评分查询成功：身份证={}，姓名={}，评分={}", 
                    maskIdCard(idCard), name, result.getCreditScore());
            
            return ApiResult.ok(result);
            
        } catch (Exception e) {
            log.error("征信评分查询失败：身份证={}，姓名={}，错误={}", 
                     maskIdCard(idCard), name, e.getMessage(), e);
            return ApiResult.fail(500, "征信评分查询失败：" + e.getMessage());
        }
    }

    /**
     * 查询批次处理状态
     * 
     * @param batchId 批次ID
     * @return 批次处理状态
     */
    @GetMapping("/batch-status/{batchId}")
    public ApiResult<Map<String, Object>> getBatchStatus(@PathVariable String batchId) {
        try {
            log.info("查询批次处理状态：批次ID={}", batchId);
            
            if (batchId == null || batchId.trim().isEmpty()) {
                return ApiResult.fail(400, "批次ID不能为空");
            }
            
            Map<String, Object> status = creditScoreQueryService.getBatchProcessingStatus(batchId);
            
            return ApiResult.ok(status);
            
        } catch (Exception e) {
            log.error("查询批次处理状态失败：批次ID={}，错误={}", batchId, e.getMessage(), e);
            return ApiResult.fail(500, "查询批次处理状态失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询所有征信评分记录
     * 
     * @param page 页码，默认0
     * @param size 页大小，默认10
     * @return 征信评分记录列表
     */
    @GetMapping("/list")
    public ApiResult<List<CreditReportMongo>> getAllCreditScores(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        
        try {
            log.info("查询征信评分记录列表：页码={}，页大小={}", page, size);
            
            // 参数验证
            if (page < 0) {
                return ApiResult.fail(400, "页码不能小于0");
            }
            
            if (size <= 0 || size > 100) {
                return ApiResult.fail(400, "页大小必须在1-100之间");
            }
            
            List<CreditReportMongo> results = creditScoreQueryService.getAllCreditScores(page, size);
            
            log.info("征信评分记录列表查询成功：页码={}，页大小={}，记录数={}", 
                    page, size, results.size());
            
            return ApiResult.ok(results);
            
        } catch (Exception e) {
            log.error("查询征信评分记录列表失败：页码={}，页大小={}，错误={}", 
                     page, size, e.getMessage(), e);
            return ApiResult.fail(500, "查询征信评分记录列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取征信评分统计信息
     * 
     * @return 征信评分统计信息
     */
    @GetMapping("/statistics")
    public ApiResult<Map<String, Object>> getCreditScoreStatistics() {
        try {
            log.info("查询征信评分统计信息");
            
            Map<String, Object> statistics = creditScoreQueryService.getCreditScoreStatistics();
            
            log.info("征信评分统计信息查询成功");
            return ApiResult.ok(statistics);
            
        } catch (Exception e) {
            log.error("查询征信评分统计信息失败：错误={}", e.getMessage(), e);
            return ApiResult.fail(500, "查询征信评分统计信息失败：" + e.getMessage());
        }
    }

    /**
     * 身份证号码掩码处理
     */
    private String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 18) {
            return idCard;
        }
        return idCard.substring(0, 6) + "****" + idCard.substring(14);
    }
}
