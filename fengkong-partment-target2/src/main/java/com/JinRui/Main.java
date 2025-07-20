package com.JinRui;

import com.sun.glass.ui.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * ClassName:${NAME}
 * Package: com.JinRui
 * Description:
 *
 * @Author Monkey
 * @Create 2025/7/8 17:25
 * @Version 1.0
 */
@SpringBootApplication
@EnableCaching
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class,args);
    }
}