package com.iserver.common.job.utils;

import cn.hutool.core.util.StrUtil;
import com.iserver.common.job.constants.IJobConst;
import com.iserver.common.job.enums.MisfireEnum;
import com.iserver.common.job.extension.IQuartzJob;
import com.iserver.common.job.extension.IQuartzJobData;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

/**
 * Quartz 工具类封装
 *
 * @author Alay
 * @date 2021-10-31 19:05
 */
@Slf4j
public class IQuartzUtil {

    /**
     * 添加新的定时任务
     *
     * @param jobData
     * @param scheduler
     */
    public static <T extends IQuartzJobData> void addNewJob(T jobData, Scheduler scheduler) {
        // 构建一个 JobDetail
        JobDetail jobDetail = IQuartzUtil.buildJobDetail(jobData);
        jobDetail.getJobDataMap().put(IJobConst.SCHEDULE_JOB_KEY, jobData);
        // 构建 Trigger
        Trigger trigger = IQuartzUtil.buildCronTrigger(jobData);
        try {
            // 将任务与时间调度器绑定,返回值最近一次要执行的时间日期
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("添加任务失败，失败信息：{}", e.getMessage());
        }
    }

    /**
     * 暂停任务
     *
     * @param jobData
     * @param scheduler
     */
    public static <T extends IQuartzJobData> void pauseJob(T jobData, Scheduler scheduler) {
        try {
            scheduler.pauseJob(IQuartzUtil.getJobKey(jobData));
        } catch (SchedulerException e) {
            log.error("暂停任务失败，失败信息：{}", e.getMessage());
        }
    }

    /**
     * 暂时挂起任务，可以用start（）再次启动
     *
     * @param scheduler
     * @param <T>
     */
    public static <T extends IQuartzJobData> void standby(Scheduler scheduler) {
        try {
            scheduler.standby();
        } catch (SchedulerException e) {
            log.error("挂起任务失败，失败信息：{}", e.getMessage());
        }
    }

    /**
     * 启动挂起的任务
     *
     * @param scheduler
     * @param <T>
     */
    public static <T extends IQuartzJobData> void start(Scheduler scheduler) {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            log.error("启动任务失败，失败信息：{}", e.getMessage());
        }
    }

    /**
     * 停止所有运行定时任务
     *
     * @param scheduler
     */
    public static void pauseAllJobs(Scheduler scheduler) {
        try {
            scheduler.pauseAll();
        } catch (SchedulerException e) {
            log.error("停止所有任务失败，失败信息：{}", e.getMessage());
        }
    }

    /**
     * 运行单个定时任务
     *
     * @param jobData
     * @param scheduler
     */
    public static <T extends IQuartzJobData> boolean runJob(T jobData, Scheduler scheduler) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(IJobConst.SCHEDULE_JOB_KEY, jobData);
        try {
            scheduler.triggerJob(IQuartzUtil.getJobKey(jobData), jobDataMap);
        } catch (SchedulerException e) {
            log.error("执行任务失败，失败信息：{}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 恢复暂停的定时任务
     *
     * @param jobData
     * @param scheduler
     * @param <T>
     */
    public static <T extends IQuartzJobData> void resumeJob(T jobData, Scheduler scheduler) {
        try {
            scheduler.resumeJob(getJobKey(jobData));
        } catch (SchedulerException e) {
            log.error("恢复任务失败，失败信息：{}", e.getMessage());
        }
    }

    /**
     * 定时任务是否已经存在
     *
     * @param jobData
     * @param scheduler
     * @param <T>
     * @return
     */
    public static <T extends IQuartzJobData> boolean checkExists(T jobData, Scheduler scheduler) {
        try {
            JobKey jobKey = getJobKey(jobData);
            return scheduler.checkExists(jobKey);
        } catch (SchedulerException e) {
            log.error("检查任务是否存在，失败信息：{}", e.getMessage());
        }
        return false;
    }

    /**
     * 启动所有运行定时任务
     *
     * @param scheduler
     */
    public void runAllJobs(Scheduler scheduler) {
        try {
            if (scheduler != null) {
                scheduler.resumeAll();
            }
        } catch (SchedulerException e) {
            log.error("启动所有任务失败，失败信息：{}", e.getMessage());
        }
    }

    /**
     * 移除定时任务
     *
     * @param jobData
     * @param scheduler
     */
    public static <T extends IQuartzJobData> boolean removeJob(T jobData, Scheduler scheduler) {
        boolean isOk = false;
        TriggerKey triggerKey = IQuartzUtil.getTriggerKey(jobData);
        try {
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // scheduler
            isOk = scheduler.unscheduleJob(triggerKey);
            if (isOk) {
                JobKey jobKey = IQuartzUtil.getJobKey(jobData);
                // 删除任务
                isOk = scheduler.deleteJob(jobKey);
            }
        } catch (SchedulerException e) {
            log.error("移除任务失败，失败信息：{}", e.getMessage());
        }
        return isOk;
    }

    /**
     * 构建一个新的 JobDetail 实例
     *
     * @param jobData
     * @return
     */
    public static <T extends IQuartzJobData> JobDetail buildJobDetail(T jobData) {
        // 新建一个工作任务 指定任务类型为串接进行的
        JobDetail jobDetail = JobBuilder.newJob(IQuartzJob.class)
                .withIdentity(IQuartzUtil.getJobKey(jobData))
                .withDescription(jobData.desc())
                .build();
        return jobDetail;
    }


    /**
     * 构建 CronTrigger
     *
     * @param jobData
     * @return
     */
    public static <T extends IQuartzJobData> Trigger buildCronTrigger(T jobData) {
        // 判断 cron 表达式是否正确
        jobData.verifyCron();
        // 触发时间点(Cron表达式构建)
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobData.cronExpression());
        // 错失周期执行策略
        MisfireEnum misfirePolicy = MisfireEnum.codeOf(jobData.misfire());
        IQuartzUtil.misfirePolicy(misfirePolicy, cronScheduleBuilder);
        // 创建触发器并将cron表达式对象给塞入
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(IQuartzUtil.getTriggerKey(jobData))
                .withSchedule(cronScheduleBuilder)
                .build();
        return cronTrigger;
    }

    /**
     * 获取任务唯一 Key
     *
     * @param jobData
     * @return
     */
    public static <T extends IQuartzJobData> JobKey getJobKey(T jobData) {
        String jobName = jobData.name();
        String jobGroup = jobData.group();
        if (StrUtil.isBlank(jobName) || StrUtil.isBlank(jobGroup)) {
            throw new RuntimeException("任务名和任务组名不能为空");
        }
        JobKey key = JobKey.jobKey(jobName, jobGroup);
        return key;
    }

    /**
     * 获取任务唯一 Key
     *
     * @return
     */
    public static <T extends IQuartzJobData> TriggerKey getTriggerKey(T jobData) {
        String jobName = jobData.name();
        String jobGroup = jobData.group();
        if (StrUtil.isBlank(jobName) || StrUtil.isBlank(jobGroup)) {
            throw new RuntimeException("任务名和任务组名不能为空");
        }
        return TriggerKey.triggerKey(jobName, jobGroup);
    }

    /**
     * 获取错失执行策略方法
     *
     * @param misfirePolicy       错失执行策略
     * @param cronScheduleBuilder CronScheduleBuilder
     */
    public static void misfirePolicy(MisfireEnum misfirePolicy, CronScheduleBuilder cronScheduleBuilder) {
        switch (misfirePolicy) {
            // 错失周期立即执行
            case IMMEDIATELY:
                cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
                break;
            // 下周期执行
            case NEXT_PERIOD:
                cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
                break;
            // 什么也不做,不做执行
            case DO_NOTHING:
                cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
            default:
                // 默认策略不做处理
                break;
        }
    }

}
