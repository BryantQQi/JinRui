package com.JinRui.task3.controller;

import com.JinRui.task3.entity.Ware;
import com.JinRui.task3.service.WareService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName:WareController
 * Package: com.JinRui.task3.controller
 * Description:
 *
 * @Author Monkey
 * @Create 2025/7/10 22:18
 * @Version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/region/ware")
public class WareController {

    private final RedisTemplate redisTemplate;
    private final WareService wareService;

    //通过仓库名字获取仓库信息
    @GetMapping("{name}")
    public Ware getWareName(@PathVariable String name){

        LambdaQueryWrapper<Ware> wraper = new LambdaQueryWrapper();
        wraper.eq(Ware::getName,name);
        Ware ware = wareService.getOne(wraper);
        //redisTemplate.opsForValue().increment(api:count::ware.getId());
        return ware;
        //test
        //test2
        //test2-1

    }

}
