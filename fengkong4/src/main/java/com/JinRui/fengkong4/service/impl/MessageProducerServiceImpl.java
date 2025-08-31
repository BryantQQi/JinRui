package com.JinRui.fengkong4.service.impl;

import com.JinRui.fengkong4.config.RabbitMQConfig;
import com.JinRui.fengkong4.service.MessageProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息发送服务实现类
 * 
 * @author JinRui
 */
@Slf4j
@Service
public class MessageProducerServiceImpl implements MessageProducerService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息到队列
     * 
     * @param message 要发送的消息内容
     */
    @Override
    public void sendMessage(String message) {
        try {
            log.info("准备发送消息: {}", message);
            
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.DEMO_EXCHANGE, 
                RabbitMQConfig.DEMO_ROUTING_KEY, 
                message
            );
            
            log.info("消息发送成功: {}", message);
            
        } catch (AmqpException e) {
            log.error("消息发送失败: {}, 错误信息: {}", message, e.getMessage());
            throw new RuntimeException("消息发送失败", e);
        }
    }
}
