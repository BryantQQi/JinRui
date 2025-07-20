package com.JinRui.task3.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * ClassName:CountsJob
 * Package: com.JinRui.task3.utils
 * Description:
 *
 * @Author Monkey
 * @Create 2025/7/11 15:11
 * @Version 1.0
 */
@Component
public class CountsJob {


    @Scheduled(fixedRate = 60000)
    public Integer getRedisValue(){

        return 1;
    }

}
