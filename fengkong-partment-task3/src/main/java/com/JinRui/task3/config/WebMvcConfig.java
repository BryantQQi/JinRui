package com.JinRui.task3.config;


import com.JinRui.task3.utils.RequestCountHandlerIntercepter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ClassName:WebMvcConfig
 * Package: com.JinRui.target2.config
 * Description:
 *
 * @Author Monkey
 * @Create 2025/7/11 13:48
 * @Version 1.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestCountHandlerIntercepter())
                .addPathPatterns("/**") // 添加拦截路径
                .excludePathPatterns("/error", "/login"); // 排除拦截路径
    }
}
