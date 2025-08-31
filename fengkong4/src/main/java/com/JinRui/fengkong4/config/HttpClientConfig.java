package com.JinRui.fengkong4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * HTTP客户端配置
 * 
 * @author JinRui
 */
@Configuration
public class HttpClientConfig {
    
    /**
     * 配置RestTemplate Bean
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        
        // 设置连接超时时间（毫秒）
        factory.setConnectTimeout(10000); // 10秒
        
        // 设置读取超时时间（毫秒）
        factory.setReadTimeout(30000); // 30秒
        
        return new RestTemplate(factory);
    }
}
