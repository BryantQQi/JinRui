package com.JinRui.fengkong4.factory;

import com.JinRui.fengkong4.parser.JsonReportParser;
import com.JinRui.fengkong4.parser.ReportParser;
import com.JinRui.fengkong4.parser.XmlReportParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 征信报文解析器工厂类（简单工厂模式）
 * 
 * @author JinRui
 */
@Slf4j
@Component
public class ReportParserFactory {
    
    @Autowired
    private XmlReportParser xmlReportParser;
    
    @Autowired
    private JsonReportParser jsonReportParser;
    
    /**
     * 根据格式获取对应的解析器
     * 
     * @param format 数据格式（XML或JSON）
     * @return 对应的解析器实例
     * @throws IllegalArgumentException 不支持的格式异常
     */
    public ReportParser getParser(String format) {
        if (format == null || format.trim().isEmpty()) {
            throw new IllegalArgumentException("数据格式不能为空");
        }
        
        String normalizedFormat = format.trim().toUpperCase();
        
        switch (normalizedFormat) {
            case "XML":
                log.debug("获取XML格式解析器");
                return xmlReportParser;
                
            case "JSON":
                log.debug("获取JSON格式解析器");
                return jsonReportParser;
                
            default:
                log.error("不支持的数据格式：{}", format);
                throw new IllegalArgumentException("不支持的数据格式：" + format + "，支持的格式：XML、JSON");
        }
    }
    
    /**
     * 根据数据内容自动检测格式并获取解析器
     * 
     * @param reportData 报文数据
     * @return 对应的解析器实例
     * @throws IllegalArgumentException 无法识别格式异常
     */
    public ReportParser getParserByContent(String reportData) {
        if (reportData == null || reportData.trim().isEmpty()) {
            throw new IllegalArgumentException("报文数据不能为空");
        }
        
        String trimmed = reportData.trim();
        
        // 检测XML格式
        if (xmlReportParser.isValidFormat(trimmed)) {
            log.debug("自动检测为XML格式");
            return xmlReportParser;
        }
        
        // 检测JSON格式
        if (jsonReportParser.isValidFormat(trimmed)) {
            log.debug("自动检测为JSON格式");
            return jsonReportParser;
        }
        
        log.error("无法识别数据格式，数据长度：{}，开头：{}", 
                 reportData.length(), 
                 reportData.substring(0, Math.min(50, reportData.length())));
        throw new IllegalArgumentException("无法识别数据格式，支持的格式：XML、JSON");
    }
    
    /**
     * 获取所有支持的格式
     * 
     * @return 支持的格式数组
     */
    public String[] getSupportedFormats() {
        return new String[]{"XML", "JSON"};
    }
    
    /**
     * 检查是否支持指定格式
     * 
     * @param format 数据格式
     * @return 是否支持
     */
    public boolean isFormatSupported(String format) {
        if (format == null || format.trim().isEmpty()) {
            return false;
        }
        
        String normalizedFormat = format.trim().toUpperCase();
        return "XML".equals(normalizedFormat) || "JSON".equals(normalizedFormat);
    }
}
