package com.JinRui.fengkong4.service;

/**
 * 消息发送服务接口
 * 
 * @author JinRui
 */
public interface MessageProducerService {
    
    /**
     * 发送消息到队列
     * 
     * @param message 要发送的消息内容
     */
    void sendMessage(String message);
}
