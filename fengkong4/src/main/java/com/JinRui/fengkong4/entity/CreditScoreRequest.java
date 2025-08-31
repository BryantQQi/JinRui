package com.JinRui.fengkong4.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 征信评分请求实体
 * 
 * @author JinRui
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditScoreRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 身份证号码
     */
    private String idCard;

    /**
     * 姓名
     */
    private String name;

    /**
     * 请求时间
     */
    private LocalDateTime requestTime;

    /**
     * 批次ID
     */
    private String batchId;

    /**
     * 构造函数 - 自动生成请求ID和时间
     */
    public CreditScoreRequest(String idCard, String name, String batchId) {
        this.requestId = UUID.randomUUID().toString();
        this.idCard = idCard;
        this.name = name;
        this.batchId = batchId;
        this.requestTime = LocalDateTime.now();
    }
}
