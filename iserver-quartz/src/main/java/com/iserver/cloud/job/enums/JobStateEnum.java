package com.iserver.cloud.job.enums;

import lombok.Getter;

/**
 * @author Alay
 * @date 2021-10-31 17:34
 */
@Getter
public enum JobStateEnum  {

    /**
     * 未发布
     */
    UN_PUBLISH(1),
    /**
     * 暂停
     */
    PAUSE(2),
    /**
     * 运行中
     */
    RUNNING(3),
    /**
     * 移除
     */
    REMOVED(4);


    /**
     * 状态码
     */
    private int code;

    JobStateEnum(int code) {
        this.code = code;
    }


    public boolean gt(JobStateEnum jobState) {
        return this.getCode() > jobState.getCode();
    }

    public boolean le(JobStateEnum jobState) {
        return this.getCode() <= jobState.getCode();
    }


}
