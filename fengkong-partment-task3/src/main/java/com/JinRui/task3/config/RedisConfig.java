package com.JinRui.task3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * ClassName:RedisConfig
 * Package: com.JinRui.utils
 * Description:
 *
 * @Author Monkey
 * @Create 2025/7/11 13:10
 * @Version 1.0
 */
@Configuration
public class RedisConfig {

    @Bean// 将该方法的返回值加入到容器
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        // 1.创建一个redis模板对象
        //这行创建了一个空的 Redis 操作模板对象，但它还不能使用，因为没有指定它该通过谁来连接 Redis。
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        //这行代码就是告诉这个 RedisTemplate,你通过哪个 Redis 客户端连接 Redis 服务器，就靠这个 redisConnectionFactory。
        redisTemplate.setConnectionFactory(redisConnectionFactory);// 设置连接器

        // 2.配置redis模板的普通键值对的序列化策略
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));

        // 3.配置redis模板的Hash键值对的序列化策略
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        // 4.返回该redis模板对象，加入到spring容器中.
        return redisTemplate;
    }
}
