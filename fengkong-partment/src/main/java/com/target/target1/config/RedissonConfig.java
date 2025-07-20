package com.target.target1.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * ClassName:RedissonConfig
 * Package: com.target.target1.config
 * Description:
 *
 * @Author Monkey
 * @Create 2025/7/8 15:41
 * @Version 1.0
 */
@Configuration
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException{
        RedissonClient redisson = Redisson
                .create(Config.fromYAML(new ClassPathResource("redisson-single.yaml")
                        .getInputStream()));
        return redisson;

    }


}
