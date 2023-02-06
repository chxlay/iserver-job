package com.iserver.common.core.result;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应代码
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应结果
     */
    private T data;

    private static <T> R<T> restResult(T data, String code, String msg) {
        return new R<>().code(code).data(data).message(msg);
    }

    public static <T> R<T> ok() {
        return restResult(null, "00000", "SUCCESS");
    }


    public static <T> R<T> ok(T data) {
        return restResult(data, "00000", "SUCCESS");
    }

    public static <T> R<T> failed() {
        return restResult(null, "FAILED", "出错了");
    }

    public static <T> R<T> failed(String code, String message) {
        return restResult(null, code, message);
    }

    public R code(String code) {
        this.code = code;
        return this;
    }

    public R message(String message) {
        this.message = message;
        return this;
    }

    public R data(T data) {
        this.data = data;
        return this;
    }
}