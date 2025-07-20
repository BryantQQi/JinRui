package com.JinRui.task3.service.impl;

import com.JinRui.task3.entity.Ware;
import com.JinRui.task3.mapper.WareMapper;
import com.JinRui.task3.service.WareService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ClassName:WareServiceImpl
 * Package: com.JinRui.task3.service.impl
 * Description:
 *
 * @Author Monkey
 * @Create 2025/7/10 22:20
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class WareServiceImpl extends ServiceImpl<WareMapper, Ware> implements WareService {

}
