package com.iserver.common.job.extension;

import com.iserver.common.job.invoker.ITaskInvoker;
import lombok.Getter;
import lombok.Setter;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;

/**
 * 任务执行的类
 * DisallowConcurrentExecution 禁止并发执行多个相同定义的JobDetail,保证上一个任务执行完后，再去执行下一个任务，这里的任务是同一个任务
 * PersistJobDataAfterExecution 将 JobDetail中的JobDataMap（对Trigger中的DataMap无效） 进行持久化,保证同一个任务的 JobDataMap 每次都是同一个实例,
 *
 * @author Alay
 * @date 2021-10-31 16:57
 * @see {cron表达式 https://www.cnblogs.com/Alay/p/15488175.html}
 * @see {参考 https://gitee.com/chxlay/iserver-job }
 */
@Getter
@Setter
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class IQuartzJob implements Job {

    /**
     * 携带任务信息的数据对象,属性名必须与JobDataMap 中 put时的 key 一致
     * 必须写 Setter
     */
    private IQuartzJobData scheduleJob;

    @Override
    public void execute(JobExecutionContext context) {
        ITaskInvoker.invoke(scheduleJob, context.getTrigger());
        // 此处可记录日志相关的一些列操作,测试项目我就省略了
    }

}
