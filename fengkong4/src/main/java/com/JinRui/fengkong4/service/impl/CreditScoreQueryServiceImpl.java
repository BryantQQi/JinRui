package com.JinRui.fengkong4.service.impl;

import com.JinRui.fengkong4.entity.mongo.CreditReportMongo;
import com.JinRui.fengkong4.repository.CreditReportMongoRepository;
import com.JinRui.fengkong4.service.CreditScoreQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 征信评分查询服务实现类
 * 
 * @author JinRui
 */
@Slf4j
@Service
public class CreditScoreQueryServiceImpl implements CreditScoreQueryService {

    @Autowired
    private CreditReportMongoRepository creditReportRepository;

    @Override
    public CreditReportMongo getCreditScoreByIdAndName(String idCard, String name) {
        try {
            log.info("查询征信评分结果：身份证={}，姓名={}", maskIdCard(idCard), name);
            
            List<CreditReportMongo> reports = creditReportRepository.findByIdNumberAndName(idCard, name);
            
            if (reports.isEmpty()) {
                log.warn("未找到征信评分记录：身份证={}，姓名={}", maskIdCard(idCard), name);
                return null;
            }
            
            // 返回最新的记录
            CreditReportMongo latestReport = reports.stream()
                    .max((r1, r2) -> r1.getUpdateTime().compareTo(r2.getUpdateTime()))
                    .orElse(reports.get(0));
            
            log.info("查询征信评分结果成功：身份证={}，姓名={}，评分={}", 
                    maskIdCard(idCard), name, latestReport.getCreditScore());
            
            return latestReport;
            
        } catch (Exception e) {
            log.error("查询征信评分结果失败：身份证={}，姓名={}，错误={}", 
                     maskIdCard(idCard), name, e.getMessage(), e);
            throw new RuntimeException("查询征信评分结果失败", e);
        }
    }

    @Override
    public Map<String, Object> getBatchProcessingStatus(String batchId) {
        try {
            log.info("查询批次处理状态：批次ID={}", batchId);
            
            // TODO: 实现真实的批次状态查询逻辑
            // 这里返回模拟状态，实际项目中需要根据批次ID查询相关记录
            Map<String, Object> status = new HashMap<>();
            status.put("batchId", batchId);
            status.put("status", "PROCESSING");
            status.put("totalCount", 100);
            status.put("processedCount", 85);
            status.put("successCount", 80);
            status.put("failedCount", 5);
            status.put("progress", "85%");
            status.put("estimatedRemainingTime", "30秒");
            
            log.info("批次状态查询完成：批次ID={}", batchId);
            return status;
            
        } catch (Exception e) {
            log.error("查询批次处理状态失败：批次ID={}，错误={}", batchId, e.getMessage(), e);
            throw new RuntimeException("查询批次处理状态失败", e);
        }
    }

    @Override
    public List<CreditReportMongo> getAllCreditScores(int page, int size) {
        try {
            log.info("查询征信评分记录列表：页码={}，页大小={}", page, size);
            
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updateTime"));
            List<CreditReportMongo> reports = creditReportRepository.findAll(pageable).getContent();
            
            log.info("征信评分记录列表查询完成：页码={}，页大小={}，记录数={}", page, size, reports.size());
            return reports;
            
        } catch (Exception e) {
            log.error("查询征信评分记录列表失败：页码={}，页大小={}，错误={}", page, size, e.getMessage(), e);
            throw new RuntimeException("查询征信评分记录列表失败", e);
        }
    }

    @Override
    public Map<String, Object> getCreditScoreStatistics() {
        try {
            log.info("开始统计征信评分分布情况");
            
            List<CreditReportMongo> allReports = creditReportRepository.findAll();
            
            Map<String, Object> statistics = new HashMap<>();
            
            // 总记录数
            int totalCount = allReports.size();
            statistics.put("totalCount", totalCount);
            
            if (totalCount == 0) {
                statistics.put("message", "暂无征信评分数据");
                return statistics;
            }
            
            // 有评分的记录数
            long scoredCount = allReports.stream()
                    .filter(report -> report.getCreditScore() != null)
                    .count();
            statistics.put("scoredCount", scoredCount);
            statistics.put("scoredRate", String.format("%.2f%%", (double) scoredCount / totalCount * 100));
            
            if (scoredCount > 0) {
                // 评分统计
                double avgScore = allReports.stream()
                        .filter(report -> report.getCreditScore() != null)
                        .mapToInt(CreditReportMongo::getCreditScore)
                        .average()
                        .orElse(0.0);
                statistics.put("averageScore", String.format("%.2f", avgScore));
                
                int maxScore = allReports.stream()
                        .filter(report -> report.getCreditScore() != null)
                        .mapToInt(CreditReportMongo::getCreditScore)
                        .max()
                        .orElse(0);
                statistics.put("maxScore", maxScore);
                
                int minScore = allReports.stream()
                        .filter(report -> report.getCreditScore() != null)
                        .mapToInt(CreditReportMongo::getCreditScore)
                        .min()
                        .orElse(0);
                statistics.put("minScore", minScore);
                
                // 评分分布
                Map<String, Long> scoreDistribution = new HashMap<>();
                scoreDistribution.put("优秀(750-850)", allReports.stream()
                        .filter(report -> report.getCreditScore() != null && report.getCreditScore() >= 750)
                        .count());
                scoreDistribution.put("良好(700-749)", allReports.stream()
                        .filter(report -> report.getCreditScore() != null && report.getCreditScore() >= 700 && report.getCreditScore() < 750)
                        .count());
                scoreDistribution.put("一般(650-699)", allReports.stream()
                        .filter(report -> report.getCreditScore() != null && report.getCreditScore() >= 650 && report.getCreditScore() < 700)
                        .count());
                scoreDistribution.put("较差(600-649)", allReports.stream()
                        .filter(report -> report.getCreditScore() != null && report.getCreditScore() >= 600 && report.getCreditScore() < 650)
                        .count());
                scoreDistribution.put("很差(300-599)", allReports.stream()
                        .filter(report -> report.getCreditScore() != null && report.getCreditScore() < 600)
                        .count());
                
                statistics.put("scoreDistribution", scoreDistribution);
            }
            
            log.info("征信评分分布统计完成：总记录数={}，已评分数={}", totalCount, scoredCount);
            return statistics;
            
        } catch (Exception e) {
            log.error("统计征信评分分布情况失败：错误={}", e.getMessage(), e);
            throw new RuntimeException("统计征信评分分布情况失败", e);
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
