package com.JinRui.fengkong4.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.JinRui.fengkong4.callback.ExportProgressCallback;
import com.JinRui.fengkong4.config.ExcelExportConfig;
import com.JinRui.fengkong4.dto.CreditReportExcelDTO;
import com.JinRui.fengkong4.entity.mongo.CreditReportMongo;
import com.JinRui.fengkong4.repository.CreditReportMongoRepository;
import com.JinRui.fengkong4.service.CreditReportExcelService;
import com.JinRui.fengkong4.utils.MemoryMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 征信报告Excel导出服务实现类
 * 
 * @author JinRui
 */
@Slf4j
@Service
public class CreditReportExcelServiceImpl implements CreditReportExcelService {

    @Autowired
    private CreditReportMongoRepository creditReportRepository;

    @Autowired
    private ExcelExportConfig excelExportConfig;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void exportAllCreditReports(HttpServletResponse response) throws IOException {
        log.info("开始导出所有征信报告数据到Excel");
        
        long startTime = System.currentTimeMillis();
        
        try {
            // 获取数据总数
            long totalCount = getCreditReportCount();
            log.info("征信报告总数量：{}", totalCount);
            
            if (totalCount == 0) {
                log.warn("没有征信报告数据可供导出");
                throw new RuntimeException("没有征信报告数据可供导出");
            }
            
            // ⚠️ 关键：一次性查询所有数据到内存 - 这会导致OOM！
            log.info("开始一次性查询所有征信报告数据到内存...");
            List<CreditReportMongo> allReports = creditReportRepository.findAll(
                Sort.by(Sort.Direction.DESC, "createTime")
            );
            
            long queryTime = System.currentTimeMillis();
            log.info("数据查询完成，耗时：{}ms，查询到{}条记录", 
                    queryTime - startTime, allReports.size());
            
            // 转换为Excel DTO
            log.info("开始转换数据为Excel格式...");
            List<CreditReportExcelDTO> excelData = convertToExcelDTO(allReports);
            
            long convertTime = System.currentTimeMillis();
            log.info("数据转换完成，耗时：{}ms，转换了{}条记录", 
                    convertTime - queryTime, excelData.size());
            
            // 设置响应头
            String fileName = "征信报告数据_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + encodedFileName);
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            
            // 使用EasyExcel写入响应流
            log.info("开始写入Excel文件到响应流...");
            EasyExcel.write(response.getOutputStream(), CreditReportExcelDTO.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("征信报告数据")
                    .doWrite(excelData);
            
            long totalTime = System.currentTimeMillis() - startTime;
            log.info("Excel导出完成！总耗时：{}ms，导出记录数：{}", totalTime, excelData.size());
            
            // 内存使用情况日志
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            long maxMemory = runtime.maxMemory();
            
            log.info("内存使用情况 - 总内存：{}MB，已用内存：{}MB，空闲内存：{}MB，最大内存：{}MB", 
                    totalMemory / 1024 / 1024, 
                    usedMemory / 1024 / 1024, 
                    freeMemory / 1024 / 1024, 
                    maxMemory / 1024 / 1024);
            
        } catch (OutOfMemoryError oom) {
            log.error("导出Excel时发生内存溢出！这正是我们要模拟的OOM场景", oom);
            throw new RuntimeException("内存不足，无法完成Excel导出。数据量过大，请联系管理员处理。", oom);
        } catch (Exception e) {
            log.error("导出Excel时发生异常", e);
            throw new RuntimeException("Excel导出失败：" + e.getMessage(), e);
        }
    }

    @Override
    public void exportCreditReportsWithLimit(HttpServletResponse response, int limit) throws IOException {
        log.info("开始导出限制数量的征信报告数据到Excel，限制数量：{}", limit);
        
        long startTime = System.currentTimeMillis();
        
        try {
            // 分页查询指定数量的数据
            Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createTime"));
            List<CreditReportMongo> reports = creditReportRepository.findAll(pageable).getContent();
            
            log.info("查询到{}条征信报告数据", reports.size());
            
            if (reports.isEmpty()) {
                log.warn("没有征信报告数据可供导出");
                throw new RuntimeException("没有征信报告数据可供导出");
            }
            
            // 转换为Excel DTO
            List<CreditReportExcelDTO> excelData = convertToExcelDTO(reports);
            
            // 设置响应头
            String fileName = "征信报告数据_限制" + limit + "条_" + 
                            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + encodedFileName);
            
            // 使用EasyExcel写入响应流
            EasyExcel.write(response.getOutputStream(), CreditReportExcelDTO.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("征信报告数据")
                    .doWrite(excelData);
            
            long totalTime = System.currentTimeMillis() - startTime;
            log.info("限制数量Excel导出完成！总耗时：{}ms，导出记录数：{}", totalTime, excelData.size());
            
        } catch (Exception e) {
            log.error("导出限制数量Excel时发生异常", e);
            throw new RuntimeException("Excel导出失败：" + e.getMessage(), e);
        }
    }

    @Override
    public long getCreditReportCount() {
        try {
            long count = creditReportRepository.count();
            log.debug("征信报告总数量：{}", count);
            return count;
        } catch (Exception e) {
            log.error("获取征信报告数量失败", e);
            throw new RuntimeException("获取数据数量失败：" + e.getMessage(), e);
        }
    }

    @Override
    public List<CreditReportExcelDTO> convertToExcelDTO(List<CreditReportMongo> reports) {
        log.debug("开始转换{}条征信报告数据为Excel DTO", reports.size());
        
        List<CreditReportExcelDTO> excelDTOs = new ArrayList<>(reports.size());
        
        for (CreditReportMongo report : reports) {
            try {
                CreditReportExcelDTO dto = new CreditReportExcelDTO();
                
                dto.setId(report.getId());
                dto.setCreditReportId(report.getCreditReportId());
                
                // 数据脱敏处理
                dto.setIdNumber(CreditReportExcelDTO.DataMaskUtil.maskIdNumber(report.getIdNumber()));
                dto.setName(CreditReportExcelDTO.DataMaskUtil.maskName(report.getName()));
                
                dto.setReportNumber(report.getReportNumber());
                dto.setGenerateTime(report.getGenerateTime());
                dto.setInstitutionCode(report.getInstitutionCode());
                dto.setQueryReason(report.getQueryReason());
                dto.setReportType(report.getReportType());
                dto.setReportQueryTime(report.getReportQueryTime());
                
                // 布尔值转中文
                dto.setHasValidCreditInfo(
                    CreditReportExcelDTO.DataMaskUtil.booleanToChinese(report.getHasValidCreditInfo())
                );
                
                dto.setFirstCreditMonthDiff(report.getFirstCreditMonthDiff());
                dto.setCreditScore(report.getCreditScore());
                dto.setScoreTime(report.getScoreTime());
                
                // 状态转中文
                dto.setScoreStatus(
                    CreditReportExcelDTO.DataMaskUtil.scoreStatusToChinese(report.getScoreStatus())
                );
                
                dto.setAiResponseTime(report.getAiResponseTime());
                dto.setCreateTime(report.getCreateTime());
                dto.setUpdateTime(report.getUpdateTime());
                
                excelDTOs.add(dto);
                
            } catch (Exception e) {
                log.warn("转换征信报告数据时出错，跳过该记录。ID：{}，错误：{}", 
                        report.getId(), e.getMessage());
                // 继续处理下一条记录，不中断整个转换过程
            }
        }
        
        log.debug("征信报告数据转换完成，成功转换{}条记录", excelDTOs.size());
        return excelDTOs;
    }

    @Override
    public void exportAllCreditReportsOptimized(HttpServletResponse response) throws IOException {
        exportAllCreditReportsWithCallback(response, new DefaultExportProgressCallback());
    }

    @Override
    public void exportAllCreditReportsWithCallback(HttpServletResponse response, ExportProgressCallback callback) throws IOException {
        log.info("开始优化版本的征信报告Excel导出");
        
        long startTime = System.currentTimeMillis();
        MemoryMonitor.logMemoryUsage("导出开始");
        
        ExcelWriter excelWriter = null;
        int successBatches = 0;
        int failedBatches = 0;
        long totalProcessed = 0;
        
        try {
            // 获取总数据量并计算批次数
            long totalCount = getCreditReportCount();
            if (totalCount == 0) {
                log.warn("没有征信报告数据可供导出");
                throw new RuntimeException("没有征信报告数据可供导出");
            }
            
            int batchSize = excelExportConfig.getBatchSize();
            int totalBatches = (int) Math.ceil((double) totalCount / batchSize);
            
            log.info("优化导出参数 - 总数据量: {}, 固定批次大小: {}, 总批次数: {}", 
                    totalCount, batchSize, totalBatches);
            
            // 设置响应头
            setupResponseHeaders(response, "征信报告数据_优化导出");
            
            // 创建EasyExcel Writer进行流式写入
            excelWriter = EasyExcel.write(response.getOutputStream(), CreditReportExcelDTO.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .build();
            WriteSheet writeSheet = EasyExcel.writerSheet("征信报告数据").build();
            
            // 分批查询和写入
            for (int currentBatch = 0; currentBatch < totalBatches; currentBatch++) {
                long batchStartTime = System.currentTimeMillis();
                
                try {
                    // 检查内存使用情况
                    double memoryUsage = MemoryMonitor.checkMemoryAndWarn(
                            excelExportConfig.getMaxMemoryUsagePercent(), 
                            "批次" + (currentBatch + 1));
                    
                    // 分页查询当前批次数据（固定批次大小为5000）
                    List<CreditReportMongo> batchReports = getCreditReportsByBatch(currentBatch, batchSize);
                    
                    if (batchReports.isEmpty()) {
                        log.info("批次 {} 没有数据，跳过", currentBatch + 1);
                        continue;
                    }
                    
                    // 转换为Excel DTO
                    List<CreditReportExcelDTO> excelData = convertToExcelDTO(batchReports);
                    
                    // 写入Excel
                    excelWriter.write(excelData, writeSheet);
                    
                    // 更新统计信息
                    totalProcessed += excelData.size();
                    successBatches++;
                    
                    long batchTime = System.currentTimeMillis() - batchStartTime;
                    MemoryMonitor.MemoryInfo memoryInfo = MemoryMonitor.getCurrentMemoryInfo();
                    
                    // 进度回调
                    callback.onBatchCompleted(currentBatch + 1, batchSize, excelData.size(), 
                            batchTime, memoryInfo.getUsedMemory(), memoryInfo.getTotalMemory());
                    
                    callback.onProgress(currentBatch + 1, totalBatches, totalProcessed, totalCount);
                    
                    // 记录进度日志
                    if (excelExportConfig.isEnableProgressLogging() && 
                        (currentBatch + 1) % excelExportConfig.getProgressReportInterval() == 0) {
                        
                        log.info("导出进度: {}/{} 批次, 已处理 {}/{} 条记录 ({:.2f}%), 批次耗时: {}ms, {}",
                                currentBatch + 1, totalBatches, 
                                totalProcessed, totalCount, 
                                (double) totalProcessed / totalCount * 100,
                                batchTime,
                                MemoryMonitor.getMemoryStatsString());
                    }
                    
                    // 清理内存
                    batchReports.clear();
                    excelData.clear();
                    
                    // 强制垃圾回收
                    if (excelExportConfig.isEnableForceGC() && 
                        (currentBatch + 1) % excelExportConfig.getProgressReportInterval() == 0) {
                        MemoryMonitor.forceGC();
                    }
                    
                    // 内存监控和警告
                    if (memoryUsage > excelExportConfig.getMaxMemoryUsagePercent()) {
                        callback.onMemoryWarning(memoryUsage, currentBatch + 1, 
                                "建议减少批次大小或执行垃圾回收");
                    }
                    
                } catch (Exception e) {
                    failedBatches++;
                    log.error("处理批次 {} 时发生异常", currentBatch + 1, e);
                    callback.onError(e, currentBatch + 1, totalProcessed);
                    
                    // 继续处理下一批次，不中断整个导出过程
                }
            }
            
            long totalTime = System.currentTimeMillis() - startTime;
            
            // 完成回调
            callback.onCompleted(totalProcessed, totalTime, successBatches, failedBatches);
            
            log.info("优化版Excel导出完成！总耗时: {}ms, 成功批次: {}, 失败批次: {}, 总处理记录: {}",
                    totalTime, successBatches, failedBatches, totalProcessed);
            
            MemoryMonitor.logMemoryUsage("导出完成");
            
        } catch (Exception e) {
            log.error("优化版Excel导出失败", e);
            callback.onError(e, -1, totalProcessed);
            throw new RuntimeException("Excel导出失败：" + e.getMessage(), e);
        } finally {
            // 确保ExcelWriter被正确关闭
            if (excelWriter != null) {
                try {
                    excelWriter.finish();
                } catch (Exception e) {
                    log.error("关闭ExcelWriter时发生异常", e);
                }
            }
        }
    }

    @Override
    public List<CreditReportMongo> getCreditReportsByBatch(int page, int batchSize) {
        try {
            Pageable pageable = PageRequest.of(page, batchSize, 
                    Sort.by(Sort.Direction.DESC, "createTime"));
            List<CreditReportMongo> reports = creditReportRepository.findAll(pageable).getContent();
            
            log.debug("分批查询完成 - 页码: {}, 批次大小: {}, 查询到记录数: {}", 
                    page, batchSize, reports.size());
            
            // 返回可修改的List，避免UnsupportedOperationException
            return new ArrayList<>(reports);
        } catch (Exception e) {
            log.error("分批查询征信报告数据失败 - 页码: {}, 批次大小: {}", page, batchSize, e);
            throw new RuntimeException("分批查询失败：" + e.getMessage(), e);
        }
    }

    /**
     * 设置HTTP响应头
     */
    private void setupResponseHeaders(HttpServletResponse response, String filePrefix) throws IOException {
        String fileName = filePrefix + "_" + 
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx";
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
        
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + encodedFileName);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }

    /**
     * 默认的导出进度回调实现
     */
    private static class DefaultExportProgressCallback implements ExportProgressCallback {
        
        @Override
        public void onProgress(int currentBatch, int totalBatches, long processedCount, long totalCount) {
            // 默认实现，不做特殊处理
        }

        @Override
        public void onBatchCompleted(int batchNumber, int batchSize, int processedCount, long batchTime, 
                                   long memoryUsed, long memoryTotal) {
            // 默认实现，不做特殊处理
        }

        @Override
        public void onError(Exception e, int currentBatch, long processedCount) {
            // 默认实现，不做特殊处理
        }

        @Override
        public void onCompleted(long totalProcessed, long totalTime, int successBatches, int failedBatches) {
            // 默认实现，不做特殊处理
        }

        @Override
        public void onMemoryWarning(double memoryUsagePercent, int currentBatch, String recommendedAction) {
            // 默认实现，不做特殊处理
        }

        @Override
        public void onBatchSizeAdjusted(int oldBatchSize, int newBatchSize, String reason) {
            // 默认实现，不做特殊处理
        }
    }
}
