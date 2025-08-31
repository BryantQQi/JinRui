package com.JinRui.fengkong4.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.JinRui.fengkong4.entity.mysql.CreditReportMysql;
import com.JinRui.fengkong4.mapper.CreditReportMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MySQL测试控制器
 * 提供MySQL数据源连接测试和基础查询功能
 * 
 * @author JinRui
 */
@Slf4j
@RestController
@RequestMapping("/api/mysql-test")
public class MysqlTestController {

    @Autowired
    private DataSource mysqlDataSource;
    
    @Autowired
    private CreditReportMapper creditReportMapper;

    /**
     * 测试MySQL连接
     * 
     * @return 连接测试结果
     */
    @GetMapping("/connection")
    public Map<String, Object> testConnection() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Connection connection = mysqlDataSource.getConnection();
            String url = connection.getMetaData().getURL();
            String username = connection.getMetaData().getUserName();
            String databaseProductName = connection.getMetaData().getDatabaseProductName();
            String databaseProductVersion = connection.getMetaData().getDatabaseProductVersion();
            
            connection.close();
            
            result.put("success", true);
            result.put("message", "MySQL连接测试成功");
            result.put("url", url);
            result.put("username", username);
            result.put("databaseProductName", databaseProductName);
            result.put("databaseProductVersion", databaseProductVersion);
            result.put("timestamp", System.currentTimeMillis());
            
            log.info("MySQL连接测试成功: {}", url);
            
        } catch (Exception e) {
            log.error("MySQL连接测试失败", e);
            result.put("success", false);
            result.put("message", "MySQL连接测试失败: " + e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
        }
        
        return result;
    }

    /**
     * 查询征信报文总数
     * 
     * @return 查询结果
     */
    @GetMapping("/count")
    public Map<String, Object> getCreditReportCount() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Integer count = creditReportMapper.selectCount(null);
            
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("count", count);
            result.put("timestamp", System.currentTimeMillis());
            
            log.info("征信报文总数: {}", count);
            
        } catch (Exception e) {
            log.error("查询征信报文总数失败", e);
            result.put("success", false);
            result.put("message", "查询失败: " + e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
        }
        
        return result;
    }

    /**
     * 分页查询征信报文
     * 
     * @param current 当前页
     * @param size 每页大小
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public Map<String, Object> getCreditReportPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            Page<CreditReportMysql> page = new Page<>(current, size);
            Page<CreditReportMysql> pageResult = creditReportMapper.selectPage(page, null);
            
            result.put("success", true);
            result.put("message", "分页查询成功");
            result.put("data", pageResult);
            result.put("timestamp", System.currentTimeMillis());
            
            log.info("分页查询征信报文成功，当前页: {}, 每页大小: {}, 总记录数: {}", 
                    current, size, pageResult.getTotal());
            
        } catch (Exception e) {
            log.error("分页查询征信报文失败", e);
            result.put("success", false);
            result.put("message", "分页查询失败: " + e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
        }
        
        return result;
    }

    /**
     * 根据身份证号查询征信报文
     * 
     * @param idNumber 身份证号
     * @return 查询结果
     */
    @GetMapping("/by-id-number/{idNumber}")
    public Map<String, Object> getCreditReportByIdNumber(@PathVariable String idNumber) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            QueryWrapper<CreditReportMysql> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id_number", idNumber);
            
            List<CreditReportMysql> list = creditReportMapper.selectList(queryWrapper);
            
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("data", list);
            result.put("count", list.size());
            result.put("timestamp", System.currentTimeMillis());
            
            log.info("根据身份证号 {} 查询征信报文，找到 {} 条记录", idNumber, list.size());
            
        } catch (Exception e) {
            log.error("根据身份证号查询征信报文失败", e);
            result.put("success", false);
            result.put("message", "查询失败: " + e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
        }
        
        return result;
    }

    /**
     * 获取API使用说明
     * 
     * @return API说明
     */
    @GetMapping("/help")
    public Map<String, Object> getHelp() {
        Map<String, Object> result = new HashMap<>();
        
        result.put("success", true);
        result.put("message", "MySQL测试API使用说明");
        result.put("apis", new String[]{
            "GET /api/mysql-test/connection - 测试MySQL连接",
            "GET /api/mysql-test/count - 查询征信报文总数",
            "GET /api/mysql-test/page?current=1&size=10 - 分页查询征信报文",
            "GET /api/mysql-test/by-id-number/{idNumber} - 根据身份证号查询征信报文",
            "GET /api/mysql-test/help - 获取API使用说明"
        });
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }
}
