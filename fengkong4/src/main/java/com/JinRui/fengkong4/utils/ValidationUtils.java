package com.JinRui.fengkong4.utils;

import java.util.regex.Pattern;

/**
 * 验证工具类
 * 
 * @author JinRui
 */
public class ValidationUtils {
    
    // 身份证号码正则表达式
    private static final String ID_CARD_PATTERN = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
    private static final Pattern ID_CARD_REGEX = Pattern.compile(ID_CARD_PATTERN);
    
    // 姓名正则表达式（中文姓名，2-10个字符）
    private static final String NAME_PATTERN = "^[\\u4e00-\\u9fa5]{2,10}$";
    private static final Pattern NAME_REGEX = Pattern.compile(NAME_PATTERN);
    
    // 手机号码正则表达式
    private static final String MOBILE_PATTERN = "^1[3-9]\\d{9}$";
    private static final Pattern MOBILE_REGEX = Pattern.compile(MOBILE_PATTERN);
    
    // 邮箱正则表达式
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_REGEX = Pattern.compile(EMAIL_PATTERN);
    
    /**
     * 验证身份证号码格式
     * 
     * @param idCard 身份证号码
     * @return 是否有效
     */
    public static boolean validateIdCard(String idCard) {
        if (idCard == null || idCard.trim().isEmpty()) {
            return false;
        }
        
        String trimmed = idCard.trim();
        
        // 基础格式验证
        if (!ID_CARD_REGEX.matcher(trimmed).matches()) {
            return false;
        }
        
        // 校验位验证
        return validateIdCardChecksum(trimmed);
    }
    
    /**
     * 验证姓名格式
     * 
     * @param name 姓名
     * @return 是否有效
     */
    public static boolean validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return NAME_REGEX.matcher(name.trim()).matches();
    }
    
    /**
     * 验证手机号码格式
     * 
     * @param mobile 手机号码
     * @return 是否有效
     */
    public static boolean validateMobile(String mobile) {
        if (mobile == null || mobile.trim().isEmpty()) {
            return false;
        }
        return MOBILE_REGEX.matcher(mobile.trim()).matches();
    }
    
    /**
     * 验证邮箱格式
     * 
     * @param email 邮箱地址
     * @return 是否有效
     */
    public static boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_REGEX.matcher(email.trim()).matches();
    }
    
    /**
     * 脱敏身份证号码
     * 
     * @param idCard 身份证号码
     * @return 脱敏后的身份证号码
     */
    public static String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 10) {
            return idCard;
        }
        return idCard.substring(0, 6) + "********" + idCard.substring(idCard.length() - 2);
    }
    
    /**
     * 脱敏手机号码
     * 
     * @param mobile 手机号码
     * @return 脱敏后的手机号码
     */
    public static String maskMobile(String mobile) {
        if (mobile == null || mobile.length() < 7) {
            return mobile;
        }
        return mobile.substring(0, 3) + "****" + mobile.substring(mobile.length() - 4);
    }
    
    /**
     * 脱敏邮箱地址
     * 
     * @param email 邮箱地址
     * @return 脱敏后的邮箱地址
     */
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        
        String[] parts = email.split("@");
        if (parts[0].length() <= 2) {
            return email;
        }
        
        String username = parts[0];
        String domain = parts[1];
        
        String maskedUsername = username.substring(0, 2) + "****";
        return maskedUsername + "@" + domain;
    }
    
    /**
     * 验证身份证号码校验位
     * 
     * @param idCard 身份证号码
     * @return 校验位是否正确
     */
    private static boolean validateIdCardChecksum(String idCard) {
        if (idCard.length() != 18) {
            return true; // 15位身份证不验证校验位
        }
        
        try {
            // 权重数组
            int[] weights = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
            // 校验码数组
            char[] checkCodes = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
            
            int sum = 0;
            for (int i = 0; i < 17; i++) {
                sum += Character.getNumericValue(idCard.charAt(i)) * weights[i];
            }
            
            int remainder = sum % 11;
            char expectedCheckCode = checkCodes[remainder];
            char actualCheckCode = Character.toUpperCase(idCard.charAt(17));
            
            return expectedCheckCode == actualCheckCode;
            
        } catch (Exception e) {
            return false;
        }
    }
}
