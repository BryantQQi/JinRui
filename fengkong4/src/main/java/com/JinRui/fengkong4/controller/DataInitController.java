package com.JinRui.fengkong4.controller;

import com.JinRui.fengkong4.service.mysql.DataInitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据初始化控制器
 * 提供MongoDB数据同步到MySQL的接口
 * 
 * @author JinRui
 */
@Slf4j
@RestController
@RequestMapping("/api/data-init")
public class DataInitController {

    @Autowired
    private DataInitService dataInitService;

    /**
     * 初始化所有数据从MongoDB到MySQL
     * 
     * @return 初始化结果
     */
    @PostMapping("/init-all")
    public Map<String, Object> initAllData() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("接收到数据初始化请求");
            String message = dataInitService.initAllData();
            
            result.put("success", true);
            result.put("message", message);
            result.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("数据初始化失败", e);
            result.put("success", false);
            result.put("message", "数据初始化失败: " + e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
        }
        
        return result;
    }

    /**
     * 清空MySQL所有表数据
     * 
     * @return 清空结果
     */
    @DeleteMapping("/clear-mysql")
    public Map<String, Object> clearMysqlData() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("接收到清空MySQL数据请求");
            String message = dataInitService.clearAllMysqlData();
            
            result.put("success", true);
            result.put("message", message);
            result.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("清空MySQL数据失败", e);
            result.put("success", false);
            result.put("message", "清空MySQL数据失败: " + e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
        }
        
        return result;
    }

    /**
     * 分别初始化各表数据
     * 
     * @param tableName 表名
     * @return 初始化结果
     */
    @PostMapping("/init-table/{tableName}")
    public Map<String, Object> initTableData(@PathVariable String tableName) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("接收到初始化表 {} 的请求", tableName);
            int count = 0;
            
            switch (tableName.toLowerCase()) {
                case "credit_reports":
                    count = dataInitService.initCreditReports();
                    break;
                case "personal_infos":
                    count = dataInitService.initPersonalInfos();
                    break;
                case "credit_transactions":
                    count = dataInitService.initCreditTransactions();
                    break;
                case "info_summaries":
                    count = dataInitService.initInfoSummaries();
                    break;
                case "query_records":
                    count = dataInitService.initQueryRecords();
                    break;
                case "public_infos":
                    count = dataInitService.initPublicInfos();
                    break;
                case "non_credit_transactions":
                    count = dataInitService.initNonCreditTransactions();
                    break;
                default:
                    throw new IllegalArgumentException("不支持的表名: " + tableName);
            }
            
            result.put("success", true);
            result.put("message", String.format("表 %s 初始化完成，共同步 %d 条记录", tableName, count));
            result.put("count", count);
            result.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("初始化表 {} 失败", tableName, e);
            result.put("success", false);
            result.put("message", String.format("初始化表 %s 失败: %s", tableName, e.getMessage()));
            result.put("timestamp", System.currentTimeMillis());
        }
        
        return result;
    }

    /**
     * 获取数据初始化状态
     * 
     * @return 状态信息
     */
    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        Map<String, Object> result = new HashMap<>();
        
        result.put("success", true);
        result.put("message", "数据初始化服务运行正常");
        result.put("timestamp", System.currentTimeMillis());
        result.put("supportedTables", new String[]{
            "credit_reports", "personal_infos", "credit_transactions", 
            "info_summaries", "query_records", "public_infos", "non_credit_transactions"
        });
        
        return result;
    }
}
