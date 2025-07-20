package com.target.target1;

import com.target.target1.controller.Users;
import com.target.target1.entity.SkuInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * ClassName:Main
 * Package: com.target.target1
 * Description:
 *
 * @Author Monkey
 * @Create 2025/7/7 15:47
 * @Version 1.0
 */
@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(Main.class, args);

    }
}
