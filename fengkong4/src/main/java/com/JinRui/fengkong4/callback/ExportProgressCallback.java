package com.JinRui.fengkong4.callback;

/**
 * Excel导出进度回调接口
 * 
 * @author JinRui
 */
public interface ExportProgressCallback {

    /**
     * 导出进度更新回调
     * 
     * @param currentBatch 当前批次号（从1开始）
     * @param totalBatches 总批次数
     * @param processedCount 已处理的记录数
     * @param totalCount 总记录数
     */
    void onProgress(int currentBatch, int totalBatches, long processedCount, long totalCount);

    /**
     * 单批处理完成回调
     * 
     * @param batchNumber 批次号
     * @param batchSize 批次大小
     * @param processedCount 本批处理的记录数
     * @param batchTime 本批处理耗时（毫秒）
     * @param memoryUsed 当前已用内存（字节）
     * @param memoryTotal 总内存（字节）
     */
    void onBatchCompleted(int batchNumber, int batchSize, int processedCount, long batchTime, 
                         long memoryUsed, long memoryTotal);

    /**
     * 处理错误回调
     * 
     * @param e 异常信息
     * @param currentBatch 发生错误的批次号
     * @param processedCount 错误发生时已处理的记录数
     */
    void onError(Exception e, int currentBatch, long processedCount);

    /**
     * 导出完成回调
     * 
     * @param totalProcessed 总处理记录数
     * @param totalTime 总耗时（毫秒）
     * @param successBatches 成功处理的批次数
     * @param failedBatches 失败的批次数
     */
    void onCompleted(long totalProcessed, long totalTime, int successBatches, int failedBatches);

    /**
     * 内存警告回调
     * 
     * @param memoryUsagePercent 内存使用百分比
     * @param currentBatch 当前批次号
     * @param recommendedAction 建议的操作
     */
    void onMemoryWarning(double memoryUsagePercent, int currentBatch, String recommendedAction);

    /**
     * 批次大小自适应调整回调
     * 
     * @param oldBatchSize 原批次大小
     * @param newBatchSize 新批次大小
     * @param reason 调整原因
     */
    void onBatchSizeAdjusted(int oldBatchSize, int newBatchSize, String reason);
}
