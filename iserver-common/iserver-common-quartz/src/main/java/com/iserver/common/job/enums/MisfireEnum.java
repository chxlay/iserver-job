package com.iserver.common.job.enums;

import lombok.Getter;

/**
 * Quartz 执行错失策略
 *
 * @author Alay
 * @date 2021-10-31 17:18
 */
@Getter
public enum MisfireEnum {
    /**
     * 默认策略,不执行
     */
    DEFAULT(0),
    /**
     * 错失周期立即执行
     */
    IMMEDIATELY(1),
    /**
     * 下周期执行
     */
    NEXT_PERIOD(2),
    /**
     * 什么也不做,不做执行
     */
    DO_NOTHING(3),
    ;


    private int code;


    MisfireEnum(int code) {
        this.code = code;
    }

    public static MisfireEnum codeOf(int code) {
        MisfireEnum[] values = MisfireEnum.values();
        for (MisfireEnum value : values) {
            if (value.code == code) {
                return value;
            }
        }
        return MisfireEnum.DEFAULT;
    }
}
