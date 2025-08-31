package com.JinRui.fengkong4.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * MyBatis-Plus字段自动填充处理器
 * 
 * @author JinRui
 */
@Slf4j
@Component
public class MetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("开始插入填充...");
        
        // 创建时间自动填充
        this.insertFill(metaObject, "createTime", Date.class, new Date());
        // 更新时间自动填充
        this.insertFill(metaObject, "updateTime", Date.class, new Date());
        // 逻辑删除字段默认值
        this.insertFill(metaObject, "deleted", Integer.class, 0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("开始更新填充...");
        
        // 更新时间自动填充
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    }
}