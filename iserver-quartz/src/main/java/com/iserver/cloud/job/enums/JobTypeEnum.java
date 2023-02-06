package com.iserver.cloud.job.enums;

import lombok.Getter;

/**
 * 任务类型
 *
 * @author Alay
 * @date 2021-10-31 17:55
 */
@Getter
public enum JobTypeEnum {
    /**
     * spring实例调用;rest调用
     */
    SPRING(1),
    /**
     * Java class
     */
    JAVA_CLASS(2),
    /**
     * REST 风格调用
     */
    REST(3);
    /**
     * 编码
     */
    private int code;


    JobTypeEnum(int code) {
        this.code = code;
    }
}
