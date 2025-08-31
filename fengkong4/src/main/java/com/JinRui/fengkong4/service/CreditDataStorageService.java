package com.JinRui.fengkong4.service;

import com.JinRui.fengkong4.entity.CreditReport;
import com.JinRui.fengkong4.parser.ParseException;

/**
 * 征信数据存储服务接口
 * 
 * @author JinRui
 */
public interface CreditDataStorageService {

    /**
     * 通过身份证号码和姓名保存对应的征信数据
     * 
     * @param idCard 身份证号码
     * @param name 姓名
     * @return 保存结果信息
     * @throws ParseException 解析异常
     */
    String saveCreditDataByIdAndName(String idCard, String name) throws ParseException;

    /**
     * 检查用户征信数据是否已存在
     * 
     * @param idCard 身份证号码
     * @param name 姓名
     * @return 是否已存在
     */
    boolean existsCreditData(String idCard, String name);

    /**
     * 根据身份证号码和姓名删除征信数据
     * 
     * @param idCard 身份证号码
     * @param name 姓名
     * @return 删除结果信息
     */
    String deleteCreditDataByIdAndName(String idCard, String name);

    /**
     * 保存征信评分结果
     * 
     * @param idCard 身份证号码
     * @param name 姓名
     * @param score 征信评分
     * @param creditReport 征信报文
     * @param aiResponseTime AI响应时间
     * @return 保存结果信息
     * @throws ParseException 解析异常
     */
    String saveCreditScoreResult(String idCard, String name, Integer score, CreditReport creditReport, Long aiResponseTime) throws ParseException;
}
