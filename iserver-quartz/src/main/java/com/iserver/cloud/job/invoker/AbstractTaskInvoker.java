package com.iserver.cloud.job.invoker;

import com.iserver.cloud.job.entity.JobInfo;
import com.iserver.cloud.job.entity.JobTaskLog;
import com.iserver.cloud.job.service.JobInfoService;
import com.iserver.cloud.job.service.TaskLogService;
import com.iserver.common.job.invoker.ITaskInvoker;
import org.quartz.CronTrigger;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneId;
import java.util.Date;

/**
 * 任务执行器抽象基类
 *
 * @author Alay
 * @date 2023-02-06 11:39
 */
public abstract class AbstractTaskInvoker implements ITaskInvoker<JobInfo> {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected JobInfoService jobInfoService;
    @Autowired
    protected TaskLogService taskLogService;

    /**
     * 任务日志保存
     *
     * @param trigger
     * @param jobInfo
     * @param taskLog
     */
    protected void taskLogRecords(Trigger trigger, JobInfo jobInfo, JobTaskLog taskLog) {

        // 记录执行时间 立刻执行使用的是 simpleTrigger
        if (trigger instanceof CronTrigger) {
            // 首次执行时间
            Date startTime = trigger.getStartTime();
            jobInfo.setFirstStart(startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            // 上次执行时间
            Date lastTime = trigger.getPreviousFireTime();
            jobInfo.setLastTime(lastTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            // 下次执行时间
            Date nextFireTime = trigger.getNextFireTime();
            jobInfo.setNextTime(nextFireTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        long endTime = System.currentTimeMillis();
        // 执行时间
        taskLog.setInvokerDuration(endTime - taskLog.getExecTime());
        // 记录日志信息
        taskLogService.save(taskLog);
    }


}
