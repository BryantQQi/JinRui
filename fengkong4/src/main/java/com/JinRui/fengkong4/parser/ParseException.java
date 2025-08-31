package com.JinRui.fengkong4.parser;

/**
 * 报文解析异常类
 * 
 * @author JinRui
 */
public class ParseException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 错误代码
     */
    private String errorCode;
    
    /**
     * 数据格式
     */
    private String format;
    
    public ParseException(String message) {
        super(message);
    }
    
    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ParseException(String message, String errorCode, String format) {
        super(message);
        this.errorCode = errorCode;
        this.format = format;
    }
    
    public ParseException(String message, Throwable cause, String errorCode, String format) {
        super(message, cause);
        this.errorCode = errorCode;
        this.format = format;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getFormat() {
        return format;
    }
    
    public void setFormat(String format) {
        this.format = format;
    }
}
