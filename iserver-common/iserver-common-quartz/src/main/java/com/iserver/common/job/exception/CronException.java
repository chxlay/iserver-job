package com.iserver.common.job.exception;

/**
 * Cron 表达式错误
 *
 * @author Alay
 * @date 2023-01-25 18:30
 */
public class CronException extends RuntimeException {

    public CronException(String message) {
        super(message);
    }
}
