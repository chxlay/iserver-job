package com.iserver.cloud.job.invoker;

import cn.hutool.core.bean.BeanUtil;
import com.iserver.cloud.job.entity.JobInfo;
import com.iserver.cloud.job.entity.JobTaskLog;
import com.iserver.cloud.job.exception.TaskException;
import com.iserver.common.core.utils.SpringBeanUtil;
import lombok.RequiredArgsConstructor;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Spring容器中获取Bean对象任务执行器
 *
 * @author Alay
 * @date 2021-10-31 18:01
 * @since
 */
@RequiredArgsConstructor
@Service(value = "SPRING")
public class SpringBeanTaskInvoker extends AbstractTaskInvoker {

    @Override
    public void doInvoke(JobInfo jobData, Trigger trigger) {
        Object returnValue;
        long beginTime = System.currentTimeMillis();
        JobTaskLog taskLog = BeanUtil.copyProperties(jobData, JobTaskLog.class);
        taskLog.setExecTime(beginTime);
        try {
            Object taskBean = SpringBeanUtil.getBeanByName(jobData.getClazzName());
            Class<?> taskClazz = taskBean.getClass();
            // 执行定时任务目标函数
            String methodParams = jobData.getParams();
            if (null != methodParams) {
                // 只允许调用 public 修改时的函数,其他修饰符的将会抛出异常
                Method taskMethod = taskClazz.getMethod(jobData.getMethodName(), String.class);
                returnValue = taskMethod.invoke(taskBean, methodParams);
            } else {
                // 只允许调用 public 修改时的函数,其他修饰符的将会抛出异常
                Method taskMethod = taskClazz.getMethod(jobData.getMethodName());
                returnValue = taskMethod.invoke(taskBean);
            }
            logger.info(">>>> 执行定时任务：{} 成功 <<<<", jobData.getJobName());
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            String message = String.format("定时任务类: %s 没有找到相关的执行函数: ,method: %s , 请确认是否存在对应参数的函数", jobData.getClazzName(), jobData.getMethodName());
            logger.error(message);
            throw new TaskException(message);
        } finally {
            this.taskLogRecords(trigger, jobData, taskLog);
        }
        if (null != returnValue) {
            logger.info("定时任务执返回数据 : {}", returnValue);
        }
        // 更新入库
        jobInfoService.updateById(jobData);
    }
}