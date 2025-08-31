package com.JinRui.fengkong4.service;

import com.JinRui.fengkong4.entity.CreditReport;

/**
 * 大模型接口模拟服务
 * 
 * @author JinRui
 */
public interface AiModelMockService {

    /**
     * 基于征信报文计算评分
     * 
     * @param creditReport 征信报文
     * @return 征信评分（300-850分）
     */
    Integer calculateCreditScore(CreditReport creditReport);

    /**
     * 模拟AI接口响应
     * 
     * @param idCard 身份证号码
     * @param name 姓名
     * @return 征信评分（300-850分）
     */
    Integer mockAiResponse(String idCard, String name);

    /**
     * 获取模拟的响应时间（毫秒）
     * 
     * @return 响应时间
     */
    Long getMockResponseTime();
}
