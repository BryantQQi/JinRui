package com.JinRui.fengkong4.controller;

import com.JinRui.fengkong4.common.ApiResult;
import com.JinRui.fengkong4.entity.CreditReport;
import com.JinRui.fengkong4.service.CreditReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 征信报文生成控制器
 * 
 * @author JinRui
 */
@Slf4j
@RestController
@RequestMapping("/api/credit-report")
public class CreditReportController {

    @Autowired
    private CreditReportService creditReportService;

    /**
     * 生成随机征信报文（XML格式）
     */
    @GetMapping(value = "/generate/random", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> generateRandomReport() {
        log.info("接收到生成随机征信报文请求");

        try {
            CreditReport report = creditReportService.generateRandomReport();
            String xml = creditReportService.convertToXml(report);

            log.info("随机征信报文生成成功，报文编号：{}", report.getHeader().getReportNumber());
            return ResponseEntity.ok(xml);
        } catch (Exception e) {
            log.error("生成随机征信报文失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("<error>征信报文生成失败：" + e.getMessage() + "</error>");
        }
    }

    /**
     * 生成自定义征信报文（JSON格式）
     */
    @GetMapping(value = "/generate/custom", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<CreditReport> generateCustomReport(
            @RequestParam(defaultValue = "5") int creditRecords,
            @RequestParam(defaultValue = "8") int queryRecords) {
        
        log.info("接收到生成自定义征信报文请求，信贷记录数：{}，查询记录数：{}", creditRecords, queryRecords);
        
        try {
            // 参数验证
            if (creditRecords < 0 || creditRecords > 20) {
                return ApiResult.fail(400, "信贷记录数必须在0-20之间");
            }
            
            if (queryRecords < 0 || queryRecords > 50) {
                return ApiResult.fail(400, "查询记录数必须在0-50之间");
            }
            
            CreditReport report = creditReportService.generateCustomReport(creditRecords, queryRecords);

            log.info("自定义征信报文生成成功，报文编号：{}", report.getHeader().getReportNumber());
            return ApiResult.ok(report);
            
        } catch (Exception e) {
            log.error("生成自定义征信报文失败", e);
            return ApiResult.fail(500, "征信报文生成失败：" + e.getMessage());
        }
    }

    /**
     * 生成征信报文XML格式
     */
    @GetMapping(value = "/generate/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> generateXmlReport(
            @RequestParam(defaultValue = "5") int creditRecords,
            @RequestParam(defaultValue = "8") int queryRecords) {
        
        log.info("接收到生成XML格式征信报文请求");
        
        try {
            CreditReport report = creditReportService.generateCustomReport(creditRecords, queryRecords);
            String xml = creditReportService.convertToXml(report);
            
            log.info("XML格式征信报文生成成功，报文编号：{}", report.getHeader().getReportNumber());
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=credit-report-" + 
                            report.getHeader().getReportNumber() + ".xml")
                    .body(xml);
            
        } catch (Exception e) {
            log.error("生成XML格式征信报文失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("<error>生成征信报文失败：" + e.getMessage() + "</error>");
        }
    }

    /**
     * 验证征信报文
     */
    @PostMapping("/validate")
    public ApiResult<Boolean> validateReport(@RequestBody CreditReport report) {
        log.info("接收到征信报文验证请求");
        
        try {
            boolean isValid = creditReportService.validateReport(report);
            log.info("征信报文验证完成，结果：{}", isValid ? "通过" : "失败");
            return ApiResult.ok(isValid);
            
        } catch (Exception e) {
            log.error("征信报文验证失败", e);
            return ApiResult.fail(500, "验证过程出错：" + e.getMessage());
        }
    }

    /**
     * 获取征信报文模板示例
     */
    @GetMapping("/template")
    public ApiResult<CreditReport> getReportTemplate() {
        log.info("接收到获取征信报文模板请求");
        
        try {
            CreditReport template = creditReportService.generateRandomReport();
            log.info("征信报文模板获取成功");
            return ApiResult.ok(template);
            
        } catch (Exception e) {
            log.error("获取征信报文模板失败", e);
            return ApiResult.fail(500, "获取模板失败：" + e.getMessage());
        }
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public ApiResult<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "fengkong4-credit-report");
        response.put("version", "1.0.0");
        response.put("description", "二代征信报文生成服务");
        return ApiResult.ok(response);
    }

    /**
     * 生成增强版征信报文（包含所有风控信息）
     */
    @GetMapping(value = "/generate/enhanced", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<CreditReport> generateEnhancedReport(
            @RequestParam(defaultValue = "5") int creditRecords,
            @RequestParam(defaultValue = "8") int queryRecords) {
        
        log.info("接收到生成增强版征信报文请求，信贷记录数：{}，查询记录数：{}", creditRecords, queryRecords);
        
        try {
            // 参数验证
            if (creditRecords < 0 || creditRecords > 20) {
                return ApiResult.fail(400, "信贷记录数必须在0-20之间");
            }
            
            if (queryRecords < 0 || queryRecords > 50) {
                return ApiResult.fail(400, "查询记录数必须在0-50之间");
            }
            
            CreditReport report = creditReportService.generateCustomReport(creditRecords, queryRecords);

            log.info("增强版征信报文生成成功，报文编号：{}", report.getHeader().getReportNumber());
            return ApiResult.ok(report);
            
        } catch (Exception e) {
            log.error("生成增强版征信报文失败", e);
            return ApiResult.fail(500, "征信报文生成失败：" + e.getMessage());
        }
    }
}
