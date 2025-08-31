package com.JinRui.fengkong4.controller;

import com.JinRui.fengkong4.common.ApiResult;
import com.JinRui.fengkong4.service.MessageProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 消息演示控制器
 * 
 * @author JinRui
 */
@Slf4j
@RestController
@RequestMapping("/api/message")
public class MessageDemoController {

    @Autowired
    private MessageProducerService messageProducerService;

    /**
     * 发送消息接口
     * 
     * @param message 要发送的消息内容
     * @return 发送结果
     */
    @PostMapping("/send")
    public ApiResult<String> sendMessage(@RequestParam String message) {
        try {
            log.info("接收到发送消息请求: {}", message);
            
            messageProducerService.sendMessage(message);
            
            return ApiResult.ok("消息发送成功: " + message);
            
        } catch (Exception e) {
            log.error("消息发送失败: {}, 错误信息: {}", message, e.getMessage());
            return ApiResult.fail(500, "消息发送失败: " + e.getMessage());
        }
    }

    /**
     * 测试接口 - 发送简单的测试消息
     * 
     * @return 发送结果
     */
    @GetMapping("/test")
    public ApiResult<String> testMessage() {
        String testMessage = "测试消息 - " + System.currentTimeMillis();
        
        try {
            log.info("发送测试消息: {}", testMessage);
            
            messageProducerService.sendMessage(testMessage);
            
            return ApiResult.ok("测试消息发送成功: " + testMessage);
            
        } catch (Exception e) {
            log.error("测试消息发送失败: {}, 错误信息: {}", testMessage, e.getMessage());
            return ApiResult.fail(500, "测试消息发送失败: " + e.getMessage());
        }
    }
}
