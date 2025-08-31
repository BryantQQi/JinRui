package com.JinRui.fengkong4.service;

import com.JinRui.fengkong4.config.RabbitMQConfig;
import com.JinRui.fengkong4.entity.CreditReport;
import com.JinRui.fengkong4.entity.CreditScoreRequest;
import com.JinRui.fengkong4.generator.CreditDataGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 征信评分消费者服务
 * 
 * @author JinRui
 */
@Slf4j
@Service
public class CreditScoreConsumerService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CreditDataGenerator creditDataGenerator;

    @Autowired
    private AiModelMockService aiModelMockService;

    @Autowired
    private CreditDataStorageService creditDataStorageService;

    /**
     * 监听并消费征信评分队列中的消息
     * 
     * @param messageBody 消息内容
     * @param message 消息对象
     * @param channel 通道对象
     */
//    @RabbitListener(queues = RabbitMQConfig.CREDIT_SCORE_QUEUE)
    public void processCreditScoreRequest(String messageBody, Message message, Channel channel) {
        long startTime = System.currentTimeMillis();
        CreditScoreRequest request = null;
        
        try {
            log.info("收到征信评分请求消息: {}", messageBody);
            
            // 解析消息
            request = objectMapper.readValue(messageBody, CreditScoreRequest.class);
            
            log.info("开始处理征信评分请求: 请求ID={}, 身份证={}, 姓名={}", 
                    request.getRequestId(), 
                    maskIdCard(request.getIdCard()), 
                    request.getName());
            
            // 处理征信评分请求
            processCreditScore(request);
            
            // 手动确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            
            long endTime = System.currentTimeMillis();
            log.info("征信评分请求处理完成: 请求ID={}, 耗时={}ms", 
                    request.getRequestId(), endTime - startTime);
            
        } catch (Exception e) {
            log.error("征信评分请求处理失败: 请求ID={}, 错误={}", 
                     request != null ? request.getRequestId() : "unknown", e.getMessage(), e);
            
            try {
                // 消息处理失败，拒绝消息并重新入队（最多重试3次）
                boolean requeue = getRetryCount(message) < 3;
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, requeue);
                
                if (!requeue) {
                    log.error("征信评分请求达到最大重试次数，消息丢弃: 请求ID={}", 
                             request != null ? request.getRequestId() : "unknown");
                }
            } catch (IOException ioException) {
                log.error("消息拒绝失败: {}", ioException.getMessage());
            }
        }
    }
    
    /**
     * 处理征信评分业务逻辑
     * 
     * @param request 征信评分请求
     */
    private void processCreditScore(CreditScoreRequest request) {
        try {
            // 1. 生成征信报文
            log.debug("开始生成征信报文: 请求ID={}", request.getRequestId());
            CreditReport creditReport = creditDataGenerator.generateCreditReport();
            
            // 2. 设置个人信息
            if (creditReport.getPersonalInfo() != null) {
                creditReport.getPersonalInfo().setIdNumber(request.getIdCard());
                creditReport.getPersonalInfo().setName(request.getName());
            }
            
            // 3. 调用大模型接口计算评分
            log.debug("开始调用大模型计算评分: 请求ID={}", request.getRequestId());
            long aiStartTime = System.currentTimeMillis();
            Integer creditScore = aiModelMockService.calculateCreditScore(creditReport);
            long aiEndTime = System.currentTimeMillis();
            long aiResponseTime = aiEndTime - aiStartTime;
            
            log.info("大模型评分计算完成: 请求ID={}, 评分={}, 耗时={}ms", 
                    request.getRequestId(), creditScore, aiResponseTime);
            
            // 4. 保存评分结果
            log.debug("开始保存评分结果: 请求ID={}", request.getRequestId());
            String saveResult = creditDataStorageService.saveCreditScoreResult(
                request.getIdCard(), request.getName(), creditScore, creditReport, aiResponseTime);
            log.info("评分结果保存完成: 请求ID={}, 结果={}", request.getRequestId(), saveResult);
            
            log.info("征信评分处理成功: 请求ID={}, 评分={}", request.getRequestId(), creditScore);
            
        } catch (Exception e) {
            log.error("征信评分处理失败: 请求ID={}, 错误={}", request.getRequestId(), e.getMessage(), e);
            throw new RuntimeException("征信评分处理失败", e);
        }
    }
    

    
    /**
     * 获取消息重试次数
     */
    private int getRetryCount(Message message) {
        Object retryCount = message.getMessageProperties().getHeaders().get("x-retry-count");
        return retryCount != null ? (Integer) retryCount : 0;
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
