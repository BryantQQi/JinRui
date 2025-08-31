package com.JinRui.fengkong4.service;

import com.JinRui.fengkong4.entity.CreditScoreRequest;

import java.util.List;

/**
 * 征信评分生产者服务接口
 * 
 * @author JinRui
 */
public interface CreditScoreProducerService {

    /**
     * 发送单个征信评分请求
     * 
     * @param request 征信评分请求
     */
    void sendCreditScoreRequest(CreditScoreRequest request);

    /**
     * 批量发送征信评分请求
     * 
     * @param requests 征信评分请求列表
     */
    void sendBatchCreditScoreRequests(List<CreditScoreRequest> requests);
}
