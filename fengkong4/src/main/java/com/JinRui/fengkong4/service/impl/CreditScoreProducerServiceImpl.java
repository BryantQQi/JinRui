package com.JinRui.fengkong4.service.impl;

import com.JinRui.fengkong4.config.RabbitMQConfig;
import com.JinRui.fengkong4.entity.CreditScoreRequest;
import com.JinRui.fengkong4.service.CreditScoreProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 征信评分生产者服务实现类
 * 
 * @author JinRui
 */
@Slf4j
@Service
public class CreditScoreProducerServiceImpl implements CreditScoreProducerService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void sendCreditScoreRequest(CreditScoreRequest request) {
        try {
            String message = objectMapper.writeValueAsString(request);
            
            log.info("准备发送征信评分请求: 请求ID={}, 身份证={}, 姓名={}", 
                    request.getRequestId(), 
                    maskIdCard(request.getIdCard()), 
                    request.getName());
            
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.CREDIT_SCORE_EXCHANGE,
                RabbitMQConfig.CREDIT_SCORE_ROUTING_KEY,
                message
            );
            
            log.info("征信评分请求发送成功: 请求ID={}", request.getRequestId());
            
        } catch (JsonProcessingException e) {
            log.error("征信评分请求序列化失败: 请求ID={}, 错误={}", request.getRequestId(), e.getMessage());
            throw new RuntimeException("征信评分请求序列化失败", e);
        } catch (AmqpException e) {
            log.error("征信评分请求发送失败: 请求ID={}, 错误={}", request.getRequestId(), e.getMessage());
            throw new RuntimeException("征信评分请求发送失败", e);
        }
    }

    @Override
    public void sendBatchCreditScoreRequests(List<CreditScoreRequest> requests) {
        log.info("开始批量发送征信评分请求，数量：{}", requests.size());
        
        int successCount = 0;
        int failCount = 0;
        
        for (CreditScoreRequest request : requests) {
            try {
                sendCreditScoreRequest(request);
                successCount++;
            } catch (Exception e) {
                failCount++;
                log.error("发送征信评分请求失败: 请求ID={}, 错误={}", request.getRequestId(), e.getMessage());
            }
        }
        
        log.info("批量发送征信评分请求完成，成功：{}，失败：{}", successCount, failCount);
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
