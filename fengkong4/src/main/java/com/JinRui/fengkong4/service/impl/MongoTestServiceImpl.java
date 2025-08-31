package com.JinRui.fengkong4.service.impl;

import com.JinRui.fengkong4.entity.MongoTestEntity;
import com.JinRui.fengkong4.repository.MongoTestRepository;
import com.JinRui.fengkong4.service.MongoTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB测试服务实现类
 * 
 * @author JinRui
 */
@Slf4j
@Service
public class MongoTestServiceImpl implements MongoTestService {

    @Autowired
    private MongoTestRepository mongoTestRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public MongoTestEntity saveTest(MongoTestEntity entity) {
        log.info("保存MongoDB测试数据，名称：{}", entity.getName());
        return mongoTestRepository.save(entity);
    }

    @Override
    public MongoTestEntity findById(String id) {
        log.info("根据ID查询MongoDB测试数据，ID：{}", id);
        Optional<MongoTestEntity> optional = mongoTestRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public List<MongoTestEntity> findByName(String name) {
        log.info("根据名称查询MongoDB测试数据，名称：{}", name);
        return mongoTestRepository.findByName(name);
    }

    @Override
    public List<MongoTestEntity> findAll() {
        log.info("查询所有MongoDB测试数据");
        return mongoTestRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        log.info("删除MongoDB测试数据，ID：{}", id);
        mongoTestRepository.deleteById(id);
    }

    @Override
    public boolean checkConnection() {
        try {
            // 尝试获取集合名称来测试连接
            mongoTemplate.getCollectionNames();
            log.info("MongoDB连接状态检查成功");
            return true;
        } catch (Exception e) {
            log.error("MongoDB连接状态检查失败", e);
            return false;
        }
    }
}
