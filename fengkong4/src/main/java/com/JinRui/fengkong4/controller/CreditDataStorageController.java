package com.JinRui.fengkong4.controller;

import com.JinRui.fengkong4.parser.ParseException;
import com.JinRui.fengkong4.service.CreditDataStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.JinRui.fengkong4.utils.ValidationUtils.maskIdCard;

/**
 * 征信数据存储控制器
 * 
 * @author JinRui
 */
@Slf4j
@RestController
@RequestMapping("/api/credit")
public class CreditDataStorageController {

    @Autowired
    private CreditDataStorageService creditDataStorageService;

    /**
     * 通过身份证号码和姓名保存对应的征信数据
     * 
     * @param idCard 身份证号码
     * @param name 姓名
     * @return 保存结果
     */
    @PostMapping("/save-data")
    public ResponseEntity<Map<String, Object>> saveCreditDataByIdAndName(
            @RequestParam("idCard") String idCard,
            @RequestParam("name") String name) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            log.info("接收到保存征信数据请求，身份证：{}，姓名：{}", maskIdCard(idCard), name);
            
            // 参数验证
            if (idCard == null || idCard.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "身份证号码不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (name == null || name.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "姓名不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 调用服务保存数据
            String result = creditDataStorageService.saveCreditDataByIdAndName(idCard.trim(), name.trim());
            
            response.put("success", true);
            response.put("message", result);
            response.put("data", null);
            
            log.info("征信数据保存成功：{}", result);
            return ResponseEntity.ok(response);
            
        } catch (ParseException e) {
            log.error("保存征信数据失败：{}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "保存征信数据失败：" + e.getMessage());
            response.put("errorCode", e.getErrorCode());
            return ResponseEntity.badRequest().body(response);
            
        } catch (Exception e) {
            log.error("保存征信数据失败：{}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "系统错误：" + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
