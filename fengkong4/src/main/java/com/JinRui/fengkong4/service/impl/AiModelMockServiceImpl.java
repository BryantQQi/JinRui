package com.JinRui.fengkong4.service.impl;

import com.JinRui.fengkong4.entity.CreditReport;
import com.JinRui.fengkong4.entity.InfoSummary;
import com.JinRui.fengkong4.service.AiModelMockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * 大模型接口模拟服务实现类
 * 
 * @author JinRui
 */
@Slf4j
@Service
public class AiModelMockServiceImpl implements AiModelMockService {

    private final Random random = new Random();

    @Override
    public Integer calculateCreditScore(CreditReport creditReport) {
        log.debug("开始基于征信报文计算评分");
        
        // 模拟AI计算响应时间（100-2000毫秒）
        long startTime = System.currentTimeMillis();
        try {
            Thread.sleep(random.nextInt(1900) + 100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 基础评分
        int baseScore = 500;
        
        // 根据征信报文数据调整评分
        if (creditReport.getInfoSummary() != null) {
            InfoSummary summary = creditReport.getInfoSummary();
            
            // 逾期账户影响
            if (summary.getOverdueAccountCount() != null && summary.getOverdueAccountCount() > 0) {
                baseScore -= summary.getOverdueAccountCount() * 30;
            }
            
            // 呆账影响
            if (summary.getBadDebtAccountCount() != null && summary.getBadDebtAccountCount() > 0) {
                baseScore -= summary.getBadDebtAccountCount() * 100;
            }
            
            // 信用额度使用率影响
            if (summary.getTotalCreditLimit() != null && summary.getUsedCreditLimit() != null 
                && summary.getTotalCreditLimit() > 0) {
                double utilizationRate = (double) summary.getUsedCreditLimit() / summary.getTotalCreditLimit();
                if (utilizationRate > 0.8) {
                    baseScore -= 50;
                } else if (utilizationRate < 0.3) {
                    baseScore += 30;
                }
            }
            
            // 账户数量正面影响
            if (summary.getCreditAccountCount() != null && summary.getCreditAccountCount() > 0) {
                baseScore += Math.min(summary.getCreditAccountCount() * 10, 50);
            }
        }
        
        // 添加随机波动（±50分）
        int randomAdjustment = random.nextInt(101) - 50;
        baseScore += randomAdjustment;
        
        // 确保评分在300-850范围内
        int finalScore = Math.max(300, Math.min(850, baseScore));
        
        long endTime = System.currentTimeMillis();
        log.debug("征信评分计算完成，评分：{}，耗时：{}ms", finalScore, endTime - startTime);
        
        return finalScore;
    }

    @Override
    public Integer mockAiResponse(String idCard, String name) {
        log.debug("模拟AI接口响应，身份证：{}，姓名：{}", 
                 maskIdCard(idCard), name);
        
        // 模拟网络延迟
        try {
            Thread.sleep(random.nextInt(1000) + 500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 基于身份证号码生成相对稳定的评分
        int hashCode = (idCard + name).hashCode();
        int baseScore = 400 + Math.abs(hashCode % 400);  // 400-800基础范围
        
        // 添加随机波动
        int randomAdjustment = random.nextInt(101) - 50;  // ±50分波动
        int finalScore = Math.max(300, Math.min(850, baseScore + randomAdjustment));
        
        log.debug("AI模拟评分完成，评分：{}", finalScore);
        return finalScore;
    }

    @Override
    public Long getMockResponseTime() {
        // 返回模拟的响应时间（500-1500毫秒）
        return (long) (random.nextInt(1000) + 500);
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
