package com.iserver.common.job.extension;

import com.iserver.common.job.enums.MisfireEnum;
import com.iserver.common.job.exception.CronException;
import org.quartz.CronExpression;

import java.io.Serializable;

/**
 * Job数据约束接口
 *
 * @author Alay
 * @date 2022-02-15 12:05
 */
public interface IQuartzJobData extends Serializable {

    /**
     * Job名称
     *
     * @return
     */
    String name();

    /**
     * Job 分组
     *
     * @return
     */
    String group();

    /**
     * Cron 表达式
     *
     * @return
     */
    String cronExpression();

    /**
     * Quartz 执行错失策略
     *
     * @return
     */
    default Integer misfire() {
        return MisfireEnum.DEFAULT.getCode();
    }

    /**
     * 任务描述
     *
     * @return
     */
    default String desc() {
        return "Quartz 定时任务";
    }


    /**
     * 任务类型
     *
     * @return
     */
    default String type() {
        return "INSTANCE";
    }

    /**
     * 判断 cron 表达式是否正确
     *
     * @return
     */
    default void verifyCron() {
        boolean isCron = CronExpression.isValidExpression(this.cronExpression());
        if (!isCron) {
            // Corn 表达式错误
            throw new CronException("错误的 Cron 表达式");
        }
    }
}
