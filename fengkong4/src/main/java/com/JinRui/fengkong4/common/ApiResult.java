package com.JinRui.fengkong4.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApiResult<T> implements Serializable {
    private int code;       // 业务状态码
    private String msg;     // 状态码含义
    private T data;         // 泛型数据
    private String traceId; // 链路ID

    public static <T> ApiResult<T> ok(T data) {
        ApiResult<T> r = new ApiResult<>();
        r.setCode(200);
        r.setMsg("success");
        r.setData(data);
        r.setTraceId(currentTraceId());
        return r;
    }

    public static <T> ApiResult<T> fail(int code, String msg) {
        ApiResult<T> r = new ApiResult<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setTraceId(currentTraceId());
        return r;
    }

    public static <T> ApiResult<T> fail(int code, String msg, T data) {
        ApiResult<T> r = new ApiResult<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        r.setTraceId(currentTraceId());
        return r;
    }

    private static String currentTraceId() {
        // 可从 MDC/Sleuth 获取链路ID，这里先返回空串占位
        return "";
    }
}


