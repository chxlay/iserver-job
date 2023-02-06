package com.iserver.common.job.support;

import com.iserver.common.core.utils.IBeanUtil;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * JobDataMap 与 Job 数据处理的工厂类
 * 用来创建每次执行的 Job 实例,规避并发访问的问,JobDetail 实例也是全新的（规避并发问题）
 * JobDataMap 保证每次都是同一个实例，需要在 Job 实现类中加入@PersistJobDataAfterExecution
 *
 * @author Alay
 * @date 2021-10-31 18:52
 */
public class IJobFactory implements JobFactory {

    /**
     * 保证每一次执行都生成一个全新的 Job 实例,JobDetail 实例也是全新的（规避并发问题）
     * JobDataMap 保证每次都是同一个实例，需要在 Job 实现类中加入@PersistJobDataAfterExecution
     *
     * @param bundle
     * @param scheduler
     * @return
     * @throws SchedulerException
     */
    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
        // Job 实例
        Job job = this.createJobInstance(bundle);
        // JobDataMap 数据读取
        JobDataMap dataMap = bundle.getJobDetail().getJobDataMap();
        // 调用Setter 函数,属性封装
        this.setBeanProps(job, dataMap);
        return job;
    }

    /**
     * 创建 Job 实例
     *
     * @param bundle
     * @return
     * @throws SchedulerException
     */
    private Job createJobInstance(TriggerFiredBundle bundle) throws SchedulerException {
        Class<? extends Job> jobClazz = bundle.getJobDetail().getJobClass();
        try {
            Constructor<? extends Job> ctor = jobClazz.getDeclaredConstructor(new Class[0]);
            // 授权
            ctor.setAccessible(true);
            Job job = ctor.newInstance();
            return job;
        } catch (Throwable throwable) {
            throw new SchedulerException("Job instantiation failed", throwable);
        }
    }


    /**
     * 通过反射 将 JobDataMap 中的数据调用 Setter 函数赋值到 Job 对象
     *
     * @param job
     * @param dataMap
     */
    private void setBeanProps(Job job, JobDataMap dataMap) {
        Iterator entryIter = dataMap.getWrappedMap().entrySet().iterator();
        try {
            while (entryIter.hasNext()) {
                Map.Entry<?, ?> entry = (Map.Entry) entryIter.next();
                // JobDataMap 中数据 key
                String fieldName = (String) entry.getKey();
                Method setMeth = IBeanUtil.setMethod(job.getClass(), fieldName);
                // 属性值
                Object dataValue = entry.getValue();
                // 反射调用 job 的Setter 函数将属性赋值
                setMeth.invoke(job, dataValue);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
