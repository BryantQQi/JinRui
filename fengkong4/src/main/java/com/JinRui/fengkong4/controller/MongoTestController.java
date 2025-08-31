package com.JinRui.fengkong4.controller;

import com.JinRui.fengkong4.common.ApiResult;
import com.JinRui.fengkong4.entity.MongoTestEntity;
import com.JinRui.fengkong4.service.MongoTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MongoDB测试控制器
 * 
 * @author JinRui
 */
@Slf4j
@RestController
@RequestMapping("/api/mongo/test")
public class MongoTestController {

    @Autowired
    private MongoTestService mongoTestService;

    /**
     * 插入测试数据
     */
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<MongoTestEntity> saveTest(@RequestBody MongoTestEntity entity) {
        log.info("接收到保存MongoDB测试数据请求，名称：{}", entity.getName());
        
        try {
            // 参数验证
            if (entity.getName() == null || entity.getName().trim().isEmpty()) {
                return ApiResult.fail(400, "测试名称不能为空");
            }
            
            if (entity.getData() == null || entity.getData().trim().isEmpty()) {
                return ApiResult.fail(400, "测试数据不能为空");
            }
            
            MongoTestEntity saved = mongoTestService.saveTest(entity);
            log.info("MongoDB测试数据保存成功，ID：{}", saved.getId());
            return ApiResult.ok(saved);
            
        } catch (Exception e) {
            log.error("保存MongoDB测试数据失败", e);
            return ApiResult.fail(500, "保存数据失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询测试数据
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<MongoTestEntity> findById(@PathVariable String id) {
        log.info("接收到根据ID查询MongoDB测试数据请求，ID：{}", id);
        
        try {
            MongoTestEntity entity = mongoTestService.findById(id);
            if (entity == null) {
                return ApiResult.fail(404, "未找到ID为 " + id + " 的测试数据");
            }
            
            log.info("根据ID查询MongoDB测试数据成功");
            return ApiResult.ok(entity);
            
        } catch (Exception e) {
            log.error("根据ID查询MongoDB测试数据失败", e);
            return ApiResult.fail(500, "查询数据失败：" + e.getMessage());
        }
    }

    /**
     * 根据名称查询测试数据
     */
    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<List<MongoTestEntity>> findByName(@PathVariable String name) {
        log.info("接收到根据名称查询MongoDB测试数据请求，名称：{}", name);
        
        try {
            List<MongoTestEntity> entities = mongoTestService.findByName(name);
            log.info("根据名称查询MongoDB测试数据成功，找到 {} 条记录", entities.size());
            return ApiResult.ok(entities);
            
        } catch (Exception e) {
            log.error("根据名称查询MongoDB测试数据失败", e);
            return ApiResult.fail(500, "查询数据失败：" + e.getMessage());
        }
    }

    /**
     * 查询所有测试数据
     */
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<List<MongoTestEntity>> findAll() {
        log.info("接收到查询所有MongoDB测试数据请求");
        
        try {
            List<MongoTestEntity> entities = mongoTestService.findAll();
            log.info("查询所有MongoDB测试数据成功，共 {} 条记录", entities.size());
            return ApiResult.ok(entities);
            
        } catch (Exception e) {
            log.error("查询所有MongoDB测试数据失败", e);
            return ApiResult.fail(500, "查询数据失败：" + e.getMessage());
        }
    }

    /**
     * 删除测试数据
     */
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<String> deleteById(@PathVariable String id) {
        log.info("接收到删除MongoDB测试数据请求，ID：{}", id);
        
        try {
            // 先检查数据是否存在
            MongoTestEntity entity = mongoTestService.findById(id);
            if (entity == null) {
                return ApiResult.fail(404, "未找到ID为 " + id + " 的测试数据");
            }
            
            mongoTestService.deleteById(id);
            log.info("删除MongoDB测试数据成功，ID：{}", id);
            return ApiResult.ok("数据删除成功");
            
        } catch (Exception e) {
            log.error("删除MongoDB测试数据失败", e);
            return ApiResult.fail(500, "删除数据失败：" + e.getMessage());
        }
    }

    /**
     * MongoDB连接状态检查
     */

    /**
     * 批量插入测试数据
     */
    @PostMapping(value = "/batch", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<String> batchSave(@RequestParam(defaultValue = "5") int count) {
        log.info("接收到批量插入MongoDB测试数据请求，数量：{}", count);
        
        try {
            if (count <= 0 || count > 100) {
                return ApiResult.fail(400, "批量插入数量必须在1-100之间");
            }
            
            for (int i = 1; i <= count; i++) {
                MongoTestEntity entity = new MongoTestEntity(
                    "测试数据" + i, 
                    "这是第" + i + "条测试数据，用于验证MongoDB功能"
                );
                mongoTestService.saveTest(entity);
            }
            
            log.info("批量插入MongoDB测试数据成功，插入 {} 条记录", count);
            return ApiResult.ok("批量插入成功，共插入 " + count + " 条数据");
            
        } catch (Exception e) {
            log.error("批量插入MongoDB测试数据失败", e);
            return ApiResult.fail(500, "批量插入失败：" + e.getMessage());
        }
    }
}
