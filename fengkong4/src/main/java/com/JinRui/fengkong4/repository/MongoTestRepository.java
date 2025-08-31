package com.JinRui.fengkong4.repository;

import com.JinRui.fengkong4.entity.MongoTestEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MongoDB测试Repository接口
 * 
 * @author JinRui
 */
@Repository
public interface MongoTestRepository extends MongoRepository<MongoTestEntity, String> {

    /**
     * 根据名称查询
     * 
     * @param name 名称
     * @return 测试实体列表
     */
    List<MongoTestEntity> findByName(String name);

    /**
     * 根据名称模糊查询
     * 
     * @param name 名称关键字
     * @return 测试实体列表
     */
    List<MongoTestEntity> findByNameContaining(String name);
}
