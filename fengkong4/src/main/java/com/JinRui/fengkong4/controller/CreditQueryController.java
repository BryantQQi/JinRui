package com.JinRui.fengkong4.controller;

import com.JinRui.fengkong4.common.ApiResult;
import com.JinRui.fengkong4.entity.TreeNode;
import com.JinRui.fengkong4.parser.ParseException;
import com.JinRui.fengkong4.service.TreeNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 征信查询控制器
 * 
 * @author JinRui
 */
@Slf4j
@RestController
@RequestMapping("/api/credit-query")
public class CreditQueryController {
    
    @Autowired
    private TreeNodeService treeNodeService;
    
    /**
     * 根据身份证和姓名查询征信报文并转换为TreeNode
     */
    @PostMapping(value = "/query-by-identity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<TreeNode> queryByIdentity(@RequestBody IdentityQueryRequest request) {
        log.info("接收到身份证姓名查询请求，身份证：{}，姓名：{}", 
                maskIdCard(request.getIdCard()), request.getName());
        
        try {
            // 参数验证
            if (request.getIdCard() == null || request.getIdCard().trim().isEmpty()) {
                return ApiResult.fail(400, "身份证号码不能为空");
            }
            
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                return ApiResult.fail(400, "姓名不能为空");
            }
            
            // 格式验证
            if (!treeNodeService.validateIdCard(request.getIdCard())) {
                return ApiResult.fail(400, "身份证号码格式不正确");
            }
            
            if (!treeNodeService.validateName(request.getName())) {
                return ApiResult.fail(400, "姓名格式不正确，请输入2-10个中文字符");
            }
            
            // 调用服务进行查询和解析
            TreeNode treeNode = treeNodeService.callAndParseReport(request.getIdCard(), request.getName());
            
            log.info("征信查询成功，返回TreeNode节点数：{}", treeNode.getNodeCount());
            return ApiResult.ok(treeNode);
            
        } catch (ParseException e) {
            log.error("征信报文解析失败", e);
            return ApiResult.fail(500, "征信报文解析失败：" + e.getMessage());
            
        } catch (Exception e) {
            log.error("征信查询失败", e);
            return ApiResult.fail(500, "征信查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取支持的API端点列表
     */
    @GetMapping("/endpoints")
    public ApiResult<String[]> getSupportedEndpoints() {
        log.info("获取支持的API端点列表");
        
        String[] endpoints = {
            "/api/credit-report/generate/random - XML格式随机征信报文",
            "/api/credit-report/generate/custom - JSON格式自定义征信报文"
        };
        
        return ApiResult.ok(endpoints);
    }
    
    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public ApiResult<String> healthCheck() {
        return ApiResult.ok("征信查询服务运行正常");
    }
    
    /**
     * 验证身份证号码格式
     */
    @GetMapping("/validate/id-card")
    public ApiResult<Boolean> validateIdCard(@RequestParam String idCard) {
        log.info("验证身份证号码格式：{}", maskIdCard(idCard));
        
        boolean isValid = treeNodeService.validateIdCard(idCard);
        
        return ApiResult.ok(isValid);
    }
    
    /**
     * 验证姓名格式
     */
    @GetMapping("/validate/name")
    public ApiResult<Boolean> validateName(@RequestParam String name) {
        log.info("验证姓名格式：{}", name);
        
        boolean isValid = treeNodeService.validateName(name);
        
        return ApiResult.ok(isValid);
    }
    
    /**
     * 获取随机API端点（用于测试）
     */
    @GetMapping("/random-endpoint")
    public ApiResult<String> getRandomEndpoint() {
        String endpoint = treeNodeService.getRandomApiEndpoint();
        log.info("获取随机API端点：{}", endpoint);
        
        return ApiResult.ok(endpoint);
    }
    
    /**
     * 脱敏身份证号码
     */
    private String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 10) {
            return idCard;
        }
        return idCard.substring(0, 6) + "********" + idCard.substring(idCard.length() - 2);
    }
    
    /**
     * 身份证姓名查询请求对象
     */
    public static class IdentityQueryRequest {
        
        /**
         * 身份证号码
         */
        private String idCard;
        
        /**
         * 姓名
         */
        private String name;
        
        public IdentityQueryRequest() {}
        
        public IdentityQueryRequest(String idCard, String name) {
            this.idCard = idCard;
            this.name = name;
        }
        
        public String getIdCard() {
            return idCard;
        }
        
        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return "IdentityQueryRequest{" +
                   "idCard='" + (idCard != null ? idCard.substring(0, 6) + "****" : null) + '\'' +
                   ", name='" + name + '\'' +
                   '}';
        }
    }
}
