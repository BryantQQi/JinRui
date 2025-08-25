package com.JinRui.fengkong4.service;

import com.JinRui.fengkong4.entity.CreditReport;

/**
 * 征信报文服务接口
 * 
 * @author JinRui
 */
public interface CreditReportService {

    /**
     * 生成随机征信报文
     * 
     * @return 征信报文对象
     */
    CreditReport generateRandomReport();

    /**
     * 生成自定义征信报文
     * 
     * @param creditRecords 信贷记录数量
     * @param queryRecords 查询记录数量
     * @return 征信报文对象
     */
    CreditReport generateCustomReport(int creditRecords, int queryRecords);

    /**
     * 将征信报文转换为XML格式
     * 
     * @param report 征信报文对象
     * @return XML字符串
     */
    String convertToXml(CreditReport report);

    /**
     * 将征信报文转换为JSON格式
     * 
     * @param report 征信报文对象
     * @return JSON字符串
     */
    String convertToJson(CreditReport report);

    /**
     * 验证征信报文数据完整性
     * 
     * @param report 征信报文对象
     * @return 验证结果
     */
    boolean validateReport(CreditReport report);
}
