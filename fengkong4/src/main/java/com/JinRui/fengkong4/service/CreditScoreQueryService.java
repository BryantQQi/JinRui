package com.JinRui.fengkong4.service;

import com.JinRui.fengkong4.entity.mongo.CreditReportMongo;

import java.util.List;
import java.util.Map;

/**
 * 征信评分查询服务接口
 * 
 * @author JinRui
 */
public interface CreditScoreQueryService {

    /**
     * 根据身份证号码和姓名查询征信评分结果
     * 
     * @param idCard 身份证号码
     * @param name 姓名
     * @return 征信评分结果
     */
    CreditReportMongo getCreditScoreByIdAndName(String idCard, String name);

    /**
     * 查询批次处理状态
     * 
     * @param batchId 批次ID
     * @return 批次处理状态信息
     */
    Map<String, Object> getBatchProcessingStatus(String batchId);

    /**
     * 查询所有征信评分记录（分页）
     * 
     * @param page 页码
     * @param size 页大小
     * @return 征信评分记录列表
     */
    List<CreditReportMongo> getAllCreditScores(int page, int size);

    /**
     * 统计征信评分分布情况
     * 
     * @return 评分分布统计
     */
    Map<String, Object> getCreditScoreStatistics();
}
