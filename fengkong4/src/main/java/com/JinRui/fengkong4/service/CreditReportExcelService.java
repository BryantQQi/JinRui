package com.JinRui.fengkong4.service;

import com.JinRui.fengkong4.callback.ExportProgressCallback;
import com.JinRui.fengkong4.dto.CreditReportExcelDTO;
import com.JinRui.fengkong4.entity.mongo.CreditReportMongo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 征信报告Excel导出服务接口
 * 
 * @author JinRui
 */
public interface CreditReportExcelService {

    /**
     * 导出所有征信报告数据到Excel
     * 注意：此方法会一次性查询所有数据到内存，用于模拟OOM场景
     * 
     * @param response HTTP响应对象
     * @throws IOException IO异常
     */
    void exportAllCreditReports(HttpServletResponse response) throws IOException;

    /**
     * 获取征信报告总数量
     * 
     * @return 征信报告总数
     */
    long getCreditReportCount();

    /**
     * 将征信报告MongoDB实体转换为Excel导出DTO
     * 
     * @param reports 征信报告实体列表
     * @return Excel导出DTO列表
     */
    List<CreditReportExcelDTO> convertToExcelDTO(List<CreditReportMongo> reports);

    /**
     * 导出指定数量的征信报告数据到Excel（用于测试不同数据量）
     * 
     * @param response HTTP响应对象
     * @param limit 导出数据限制数量
     * @throws IOException IO异常
     */
    void exportCreditReportsWithLimit(HttpServletResponse response, int limit) throws IOException;

    /**
     * 优化版本：分批导出所有征信报告数据到Excel
     * 使用分批查询和流式写入避免OOM
     * 
     * @param response HTTP响应对象
     * @throws IOException IO异常
     */
    void exportAllCreditReportsOptimized(HttpServletResponse response) throws IOException;

    /**
     * 带进度回调的优化版本导出
     * 
     * @param response HTTP响应对象
     * @param callback 进度回调接口
     * @throws IOException IO异常
     */
    void exportAllCreditReportsWithCallback(HttpServletResponse response, ExportProgressCallback callback) throws IOException;

    /**
     * 分批查询征信报告数据
     * 
     * @param page 页码（从0开始）
     * @param batchSize 批次大小
     * @return 征信报告数据列表
     */
    List<CreditReportMongo> getCreditReportsByBatch(int page, int batchSize);
}
