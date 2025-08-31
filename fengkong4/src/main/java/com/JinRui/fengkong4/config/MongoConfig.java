package com.JinRui.fengkong4.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB配置类
 * 
 * @author JinRui
 */
@Configuration
@Primary
@EnableMongoRepositories(basePackages = "com.JinRui.fengkong4.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "fengkong4_test";
    }
}
