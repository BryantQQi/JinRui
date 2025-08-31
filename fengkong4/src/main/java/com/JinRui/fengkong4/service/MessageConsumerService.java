package com.JinRui.fengkong4.service;

import com.JinRui.fengkong4.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 消息消费服务
 * 
 * @author JinRui
 */
@Slf4j
@Service
public class MessageConsumerService {

    /**
     * 监听并消费队列中的消息
     * 
     * @param messageBody 消息内容
     * @param message 消息对象
     * @param channel 通道对象
     */
    @RabbitListener(queues = RabbitMQConfig.DEMO_QUEUE)
    public void receiveMessage(String messageBody, Message message, Channel channel) {
        try {
            log.info("收到消息: {}", messageBody);
            
            // 这里可以添加具体的业务逻辑处理
            // 模拟业务处理
            processMessage(messageBody);
            
            // 手动确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("消息处理完成并确认: {}", messageBody);
            
        } catch (Exception e) {
            log.error("消息处理失败: {}, 错误信息: {}", messageBody, e.getMessage());
            
            try {
                // 消息处理失败，拒绝消息并重新入队
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException ioException) {
                log.error("消息拒绝失败: {}", ioException.getMessage());
            }
        }
    }
    
    /**
     * 处理消息的业务逻辑
     * 
     * @param messageBody 消息内容
     */
    private void processMessage(String messageBody) {
        // 模拟业务处理逻辑
        log.info("正在处理消息业务逻辑: {}", messageBody);
        
        // 这里可以添加具体的业务处理代码
        // 例如：数据库操作、调用其他服务等
        
        log.info("消息业务逻辑处理完成: {}", messageBody);
    }
}
