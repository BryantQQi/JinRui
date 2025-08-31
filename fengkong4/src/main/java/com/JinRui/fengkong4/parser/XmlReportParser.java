package com.JinRui.fengkong4.parser;

import com.JinRui.fengkong4.entity.CreditReport;
import com.JinRui.fengkong4.entity.TreeNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

/**
 * XML格式征信报文解析器
 * 
 * @author JinRui
 */
@Slf4j
@Component
public class XmlReportParser implements ReportParser {
    
    private final XmlMapper xmlMapper = new XmlMapper();
    
    @Override
    public TreeNode parse(String reportData, String format) throws ParseException {
        log.info("开始解析XML格式征信报文，数据长度：{}", reportData.length());
        
        try {
            // 验证格式
            if (!isValidFormat(reportData)) {
                throw new ParseException("无效的XML格式", "INVALID_XML_FORMAT", format);
            }
            
            // 解析XML为CreditReport对象
            CreditReport creditReport = xmlMapper.readValue(reportData, CreditReport.class);
            
            // 转换为TreeNode
            TreeNode rootNode = convertToTreeNode(creditReport, "CreditReport", "二代征信报文主体");
            rootNode.setNodeType(TreeNode.NodeType.ROOT);
            rootNode.setLevel(0);
            
            log.info("XML格式征信报文解析完成，节点总数：{}", rootNode.getNodeCount());
            return rootNode;
            
        } catch (Exception e) {
            log.error("解析XML格式征信报文失败", e);
            throw new ParseException("XML解析失败：" + e.getMessage(), e, "XML_PARSE_ERROR", format);
        }
    }
    
    @Override
    public CreditReport parseToCreditReport(String reportData, String format) throws ParseException {
        log.info("开始解析XML格式征信报文为CreditReport对象，数据长度：{}", reportData.length());
        
        try {
            // 验证格式
            if (!isValidFormat(reportData)) {
                throw new ParseException("无效的XML格式", "INVALID_XML_FORMAT", format);
            }
            
            // 解析XML为CreditReport对象
            CreditReport creditReport = xmlMapper.readValue(reportData, CreditReport.class);
            
            log.info("XML格式征信报文解析为CreditReport对象完成");
            return creditReport;
            
        } catch (Exception e) {
            log.error("解析XML格式征信报文为CreditReport对象失败", e);
            throw new ParseException("XML解析失败：" + e.getMessage(), e, "XML_PARSE_ERROR", format);
        }
    }
    
    @Override
    public String getSupportedFormat() {
        return "XML";
    }
    
    @Override
    public boolean isValidFormat(String reportData) {
        if (reportData == null || reportData.trim().isEmpty()) {
            return false;
        }
        
        String trimmed = reportData.trim();
        return trimmed.startsWith("<") && trimmed.endsWith(">") && 
               trimmed.contains("<CreditReport");
    }
    
    /**
     * 将对象转换为TreeNode
     */
    private TreeNode convertToTreeNode(Object obj, String nodeName, String description) {
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
                TreeNode itemNode = convertToTreeNode(item, nodeName + "[" + i + "]", "数组元素 " + i);
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
                    TreeNode childNode = convertToTreeNode(fieldValue, fieldName, fieldDescription);
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
        // 这里可以根据字段名称返回对应的中文描述
        // 为了简化，返回字段名称本身
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
            default: return fieldName;
        }
    }
}
