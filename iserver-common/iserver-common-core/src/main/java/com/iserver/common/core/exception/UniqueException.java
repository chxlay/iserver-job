package com.iserver.common.core.exception;


/**
 * 数据唯一性异常
 *
 * @author Alay
 * @date 2021-08-22 04:00
 */
public class UniqueException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UniqueException(String code) {
        super(code);
    }

    public UniqueException() {
        super("数据唯一性异常");
    }
}
