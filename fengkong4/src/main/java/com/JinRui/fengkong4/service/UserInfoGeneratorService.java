package com.JinRui.fengkong4.service;

import com.JinRui.fengkong4.entity.CreditScoreRequest;

import java.util.List;

/**
 * 用户信息生成服务接口
 * 
 * @author JinRui
 */
public interface UserInfoGeneratorService {

    /**
     * 批量生成用户信息
     * 
     * @param count 生成数量
     * @param batchId 批次ID
     * @return 用户信息列表
     */
    List<CreditScoreRequest> generateUserInfoBatch(int count, String batchId);

    /**
     * 生成单个用户信息
     * 
     * @param batchId 批次ID
     * @return 用户信息
     */
    CreditScoreRequest generateSingleUserInfo(String batchId);
}
