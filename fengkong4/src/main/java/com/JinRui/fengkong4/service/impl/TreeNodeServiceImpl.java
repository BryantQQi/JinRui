package com.JinRui.fengkong4.service.impl;

import com.JinRui.fengkong4.entity.CreditReport;
import com.JinRui.fengkong4.entity.TreeNode;
import com.JinRui.fengkong4.factory.ReportParserFactory;
import com.JinRui.fengkong4.parser.ParseException;
import com.JinRui.fengkong4.parser.ReportParser;
import com.JinRui.fengkong4.service.TreeNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * TreeNode转换和操作服务实现类
 * 
 * @author JinRui
 */
@Slf4j
@Service
public class TreeNodeServiceImpl implements TreeNodeService {
    
    @Autowired
    private ReportParserFactory parserFactory;
    
    @Autowired
    private RestTemplate restTemplate;
    
    private final Random random = new Random();
    
    // 身份证号码正则表达式
    private static final String ID_CARD_PATTERN = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
    private static final Pattern ID_CARD_REGEX = Pattern.compile(ID_CARD_PATTERN);
    
    // 姓名正则表达式（中文姓名，2-10个字符）
    private static final String NAME_PATTERN = "^[\\u4e00-\\u9fa5]{2,10}$";
    private static final Pattern NAME_REGEX = Pattern.compile(NAME_PATTERN);
    
    // API端点配置
    private static final String[] API_ENDPOINTS = {
        "/api/credit-report/generate/random",
        "/api/credit-report/generate/custom"
    };
    
    private static final String BASE_URL = "http://localhost:8304";
    
    @Override
    public TreeNode convertCreditReportToTree(CreditReport report) {
        log.info("开始将CreditReport对象转换为TreeNode");
        
        if (report == null) {
            log.warn("CreditReport对象为空，返回空TreeNode");
            TreeNode emptyNode = new TreeNode("CreditReport", null, "null");
            emptyNode.setNodeType(TreeNode.NodeType.ROOT);
            emptyNode.setIsEmpty(true);
            return emptyNode;
        }
        
        try {
            TreeNode rootNode = convertObjectToTreeNode(report, "CreditReport", "二代征信报文主体");
            rootNode.setNodeType(TreeNode.NodeType.ROOT);
            rootNode.setLevel(0);
            
            log.info("CreditReport转换为TreeNode完成，节点总数：{}", rootNode.getNodeCount());
            return rootNode;
            
        } catch (Exception e) {
            log.error("转换CreditReport为TreeNode失败", e);
            throw new RuntimeException("转换失败：" + e.getMessage(), e);
        }
    }
    
    @Override
    public TreeNode callAndParseReport(String idCard, String name) throws ParseException {
        log.info("开始调用征信接口，身份证：{}，姓名：{}", maskIdCard(idCard), name);
        
        try {
            // 参数验证
            if (!validateIdCard(idCard)) {
                throw new ParseException("身份证号码格式不正确", "INVALID_ID_CARD", "VALIDATION");
            }
            
            if (!validateName(name)) {
                throw new ParseException("姓名格式不正确", "INVALID_NAME", "VALIDATION");
            }
            
            // 随机选择API端点
            String endpoint = getRandomApiEndpoint();
            log.info("随机选择的API端点：{}", endpoint);
            
            // 调用征信接口
            ReportDataResult result = callCreditReportApi(endpoint, idCard, name);
            
            // 解析报文
            ReportParser parser = parserFactory.getParserByContent(result.getData());
            TreeNode treeNode = parser.parse(result.getData(), result.getFormat());
            
            log.info("征信报文调用和解析完成，节点总数：{}", treeNode.getNodeCount());
            return treeNode;
            
        } catch (ParseException e) {
            throw e;
        } catch (Exception e) {
            log.error("调用征信接口失败", e);
            throw new ParseException("调用征信接口失败：" + e.getMessage(), e, "API_CALL_ERROR", "HTTP");
        }
    }
    
    @Override
    public String getRandomApiEndpoint() {
        int index = random.nextInt(API_ENDPOINTS.length);
        return API_ENDPOINTS[index];
    }
    
    @Override
    public boolean validateIdCard(String idCard) {
        if (idCard == null || idCard.trim().isEmpty()) {
            return false;
        }
        return ID_CARD_REGEX.matcher(idCard.trim()).matches();
    }
    
    @Override
    public boolean validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return NAME_REGEX.matcher(name.trim()).matches();
    }
    
    @Override
    public ReportDataResult callCreditReportApi(String endpoint, String idCard, String name) throws Exception {
        String url = BASE_URL + endpoint;
        log.info("调用征信接口：{}", url);
        
        try {
            HttpHeaders headers = new HttpHeaders();
            
            if (endpoint.contains("/generate/random")) {
                // XML接口
                headers.setAccept(java.util.Arrays.asList(MediaType.APPLICATION_XML));
                HttpEntity<String> entity = new HttpEntity<>(headers);
                
                ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class);
                
                if (response.getStatusCode() == HttpStatus.OK) {
                    return new ReportDataResult(response.getBody(), "XML");
                } else {
                    throw new Exception("API调用失败，状态码：" + response.getStatusCode());
                }
                
            } else if (endpoint.contains("/generate/custom")) {
                // JSON接口
                headers.setAccept(java.util.Arrays.asList(MediaType.APPLICATION_JSON));
                HttpEntity<String> entity = new HttpEntity<>(headers);
                
                // 添加随机参数
                String urlWithParams = url + "?creditRecords=" + (random.nextInt(10) + 1) + 
                                      "&queryRecords=" + (random.nextInt(20) + 5);
                
                ResponseEntity<String> response = restTemplate.exchange(
                    urlWithParams, HttpMethod.GET, entity, String.class);
                
                if (response.getStatusCode() == HttpStatus.OK) {
                    // 提取data字段的内容
                    String responseBody = response.getBody();
                    // 这里需要从ApiResult中提取CreditReport数据
                    String creditReportJson = extractCreditReportFromApiResult(responseBody);
                    return new ReportDataResult(creditReportJson, "JSON");
                } else {
                    throw new Exception("API调用失败，状态码：" + response.getStatusCode());
                }
            } else {
                throw new Exception("不支持的API端点：" + endpoint);
            }
            
        } catch (Exception e) {
            log.error("调用征信接口异常：{}", e.getMessage());
            throw new Exception("征信接口调用失败：" + e.getMessage(), e);
        }
    }
    
    /**
     * 将对象转换为TreeNode
     */
    private TreeNode convertObjectToTreeNode(Object obj, String nodeName, String description) {
        if (obj == null) {
            TreeNode node = new TreeNode(nodeName, null, "null");
            node.setDescription(description);
            node.setIsEmpty(true);
            return node;
        }
        
        Class<?> clazz = obj.getClass();
        
        // 基本数据类型和字符串
        if (isSimpleType(clazz)) {
            TreeNode node = new TreeNode(nodeName, obj, clazz.getSimpleName());
            node.setDescription(description);
            node.setIsEmpty(false);
            return node;
        }
        
        // 列表类型
        if (obj instanceof List) {
            TreeNode arrayNode = new TreeNode(nodeName, TreeNode.NodeType.ARRAY, description);
            List<?> list = (List<?>) obj;
            
            for (int i = 0; i < list.size(); i++) {
                Object item = list.get(i);
                TreeNode itemNode = convertObjectToTreeNode(item, nodeName + "[" + i + "]", "数组元素 " + i);
                itemNode.setNodeType(TreeNode.NodeType.ARRAY_ITEM);
                arrayNode.addChild(itemNode);
            }
            
            return arrayNode;
        }
        
        // 复杂对象类型
        TreeNode objectNode = new TreeNode(nodeName, TreeNode.NodeType.OBJECT, description);
        
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object fieldValue = field.get(obj);
                
                if (fieldValue != null) {
                    String fieldName = field.getName();
                    String fieldDescription = getFieldDescription(fieldName);
                    TreeNode childNode = convertObjectToTreeNode(fieldValue, fieldName, fieldDescription);
                    objectNode.addChild(childNode);
                }
            }
        } catch (Exception e) {
            log.warn("转换对象字段时出错：{}", e.getMessage());
        }
        
        return objectNode;
    }
    
    /**
     * 判断是否为简单数据类型
     */
    private boolean isSimpleType(Class<?> clazz) {
        return clazz.isPrimitive() || 
               clazz == String.class ||
               clazz == Integer.class ||
               clazz == Long.class ||
               clazz == Double.class ||
               clazz == Float.class ||
               clazz == Boolean.class ||
               clazz == Short.class ||
               clazz == Byte.class ||
               clazz == Character.class;
    }
    
    /**
     * 获取字段描述
     */
    private String getFieldDescription(String fieldName) {
        switch (fieldName) {
            case "header": return "报文头";
            case "personalInfo": return "个人基本信息";
            case "infoSummary": return "信息概要";
            case "creditTransactions": return "信贷交易信息明细";
            case "nonCreditTransactions": return "非信贷交易信息明细";
            case "publicInfos": return "公共信息明细";
            case "queryRecords": return "查询记录";
            case "statementInfo": return "标注及声明信息";
            case "fraudWarnings": return "防欺诈提示";
            case "employmentHistory": return "职业历史";
            case "residenceHistory": return "居住历史";
            case "creditSummary": return "信用概要增强";
            case "creditLimits": return "授信额度";
            case "name": return "姓名";
            case "idNumber": return "身份证号码";
            case "reportNumber": return "报文编号";
            case "generateTime": return "生成时间";
            case "version": return "版本号";
            case "institutionCode": return "机构代码";
            case "queryReason": return "查询原因";
            case "mobile": return "手机号码";
            case "email": return "电子邮箱";
            case "address": return "地址";
            case "companyName": return "单位名称";
            case "annualIncome": return "年收入";
            default: return fieldName;
        }
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
     * 从ApiResult中提取CreditReport的JSON数据
     */
    private String extractCreditReportFromApiResult(String apiResultJson) {
        try {
            // 简单的JSON提取，实际应用中可以使用JsonNode
            int dataStart = apiResultJson.indexOf("\"data\":");
            if (dataStart == -1) {
                throw new RuntimeException("无法找到data字段");
            }
            
            int dataValueStart = apiResultJson.indexOf("{", dataStart);
            if (dataValueStart == -1) {
                throw new RuntimeException("data字段格式错误");
            }
            
            // 找到匹配的右大括号
            int braceCount = 1;
            int pos = dataValueStart + 1;
            while (pos < apiResultJson.length() && braceCount > 0) {
                char c = apiResultJson.charAt(pos);
                if (c == '{') {
                    braceCount++;
                } else if (c == '}') {
                    braceCount--;
                }
                pos++;
            }
            
            return apiResultJson.substring(dataValueStart, pos);
            
        } catch (Exception e) {
            log.error("提取CreditReport数据失败", e);
            // 如果提取失败，返回原始数据
            return apiResultJson;
        }
    }
}
