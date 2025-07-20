package com.target.target1.controller;

import com.target.target1.entity.SkuInfo;
import com.target.target1.mapper.SkuInfoMapper;
import com.target.target1.service.SkuInfoService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * ClassName:User
 * Package: com.target.target1.controller
 * Description:
 *
 * @Author Monkey
 * @Create 2025/7/7 15:46
 * @Version 1.0
 */
@Data
@RestController
@RequestMapping("/aa/bb")
@RequiredArgsConstructor
public class Users {
    private final SkuInfoService skuInfoService;


    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private RedissonClient redissonClient;

    //下单方法
    @PutMapping("/placeorder/{skuid}/{numbers}")
    public SkuInfo placeOrder(@PathVariable int skuid,@PathVariable int numbers) throws InterruptedException {
        String key = "target1_redisson_lock_" + skuid;
        RLock lock = redissonClient.getLock(key);
        SkuInfo goods = null;


        //lock.lock();
        //次尝试获取锁并设置过期时间,
        boolean locked = lock.tryLock(20, 10,TimeUnit.SECONDS);
        //lock.lock();
        if(locked){
            //操作数据库
            //通过skuid找到商品
            goods = skuInfoService.getById(skuid);
            try {
                Thread.sleep(60000);
                //库存为零的校验
                if (goods.getStock() == 0){
                    return goods;
                }
                //库存为负数的校验
                if ((goods.getStock() - numbers) < 0){
                    return goods;
                }
                //进行库存扣减，并更新到数据库
                goods.setStock(goods.getStock() - numbers);
                skuInfoService.updateById(goods);
            }
            finally {
                lock.unlock();

            }
        }



        return goods;

    }

}
