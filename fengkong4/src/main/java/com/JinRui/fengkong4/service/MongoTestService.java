package com.JinRui.fengkong4.service;

import com.JinRui.fengkong4.entity.MongoTestEntity;

import java.util.List;

/**
 * MongoDB测试服务接口
 * 
 * @author JinRui
 */
public interface MongoTestService {

    /**
     * 保存测试数据
     * 
     * @param entity 测试实体
     * @return 保存后的实体
     */
    MongoTestEntity saveTest(MongoTestEntity entity);

    /**
     * 根据ID查询
     * 
     * @param id 主键ID
     * @return 测试实体
     */
    MongoTestEntity findById(String id);

    /**
     * 根据名称查询
     * 
     * @param name 名称
     * @return 测试实体列表
     */
    List<MongoTestEntity> findByName(String name);

    /**
     * 查询所有数据
     * 
     * @return 测试实体列表
     */
    List<MongoTestEntity> findAll();

    /**
     * 删除数据
     * 
     * @param id 主键ID
     */
    void deleteById(String id);

    /**
     * 检查MongoDB连接状态
     * 
     * @return 连接状态
     */
    boolean checkConnection();
}
