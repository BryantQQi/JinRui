package com.JinRui.fengkong4.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 * 
 * @author JinRui
 */
@Configuration
@EnableRabbit
public class RabbitMQConfig {

    /**
     * 演示队列名称
     */
    public static final String DEMO_QUEUE = "demo.queue";
    
    /**
     * 演示交换机名称
     */
    public static final String DEMO_EXCHANGE = "demo.exchange";
    
    /**
     * 演示路由键
     */
    public static final String DEMO_ROUTING_KEY = "demo.routing.key";

    /**
     * 征信评分队列名称
     */
    public static final String CREDIT_SCORE_QUEUE = "credit.score.queue";
    
    /**
     * 征信评分交换机名称
     */
    public static final String CREDIT_SCORE_EXCHANGE = "credit.score.exchange";
    
    /**
     * 征信评分路由键
     */
    public static final String CREDIT_SCORE_ROUTING_KEY = "credit.score.routing.key";

    /**
     * 创建演示队列
     */
    @Bean
    public Queue demoQueue() {
        return QueueBuilder.durable(DEMO_QUEUE).build();
    }

    /**
     * 创建演示交换机
     */
    @Bean
    public TopicExchange demoExchange() {
        return new TopicExchange(DEMO_EXCHANGE);
    }

    /**
     * 绑定队列到交换机
     */
    @Bean
    public Binding demoBinding() {
        return BindingBuilder.bind(demoQueue())
                .to(demoExchange())
                .with(DEMO_ROUTING_KEY);
    }

    /**
     * 创建征信评分队列
     */
    @Bean
    public Queue creditScoreQueue() {
        return QueueBuilder.durable(CREDIT_SCORE_QUEUE).build();
    }

    /**
     * 创建征信评分交换机
     */
    @Bean
    public TopicExchange creditScoreExchange() {
        return new TopicExchange(CREDIT_SCORE_EXCHANGE);
    }

    /**
     * 绑定征信评分队列到交换机
     */
    @Bean
    public Binding creditScoreBinding() {
        return BindingBuilder.bind(creditScoreQueue())
                .to(creditScoreExchange())
                .with(CREDIT_SCORE_ROUTING_KEY);
    }

    /**
     * 配置RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        
        // 设置发送确认回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                System.out.println("消息发送确认成功");
            } else {
                System.out.println("消息发送确认失败: " + cause);
            }
        });
        
        // 设置返回回调
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println("消息返回: " + new String(message.getBody()) + 
                             ", 交换机: " + exchange + 
                             ", 路由键: " + routingKey + 
                             ", 原因: " + replyText);
        });
        
        return rabbitTemplate;
    }
}
