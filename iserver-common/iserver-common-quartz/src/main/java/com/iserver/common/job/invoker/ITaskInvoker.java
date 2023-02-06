package com.iserver.common.job.invoker;

import com.iserver.common.core.utils.SpringBeanUtil;
import com.iserver.common.job.extension.IQuartzJobData;
import org.quartz.Trigger;

/**
 * 任务执行者(侧率模式)
 *
 * @author Alay
 * @date 2021-10-31 17:50
 */
public interface ITaskInvoker<T extends IQuartzJobData> {

    /**
     * 执行任务
     *
     * @param jobData
     * @param trigger
     */
    void doInvoke(T jobData, Trigger trigger);

    /**
     * 分配执行器执行任务任务
     *
     * @param jobData
     * @param trigger
     */
    static <T extends IQuartzJobData> void invoke(T jobData, Trigger trigger) {
        ITaskInvoker invoker = (ITaskInvoker) SpringBeanUtil.getBeanByName(jobData.type());
        invoker.doInvoke(jobData, trigger);
    }

}