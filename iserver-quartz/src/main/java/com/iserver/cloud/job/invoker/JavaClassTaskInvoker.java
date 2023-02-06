package com.iserver.cloud.job.invoker;

import cn.hutool.core.bean.BeanUtil;
import com.iserver.cloud.job.entity.JobInfo;
import com.iserver.cloud.job.entity.JobTaskLog;
import com.iserver.cloud.job.exception.TaskException;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Java 类信息任务执行器
 *
 * @author Alay
 * @date 2023-02-06 11:37
 */
@Service(value = "JAVA_CLASS")
public class JavaClassTaskInvoker extends AbstractTaskInvoker {


    @Override
    public void doInvoke(JobInfo jobData, Trigger trigger) {
        Object returnValue;
        long beginTime = System.currentTimeMillis();
        JobTaskLog taskLog = BeanUtil.copyProperties(jobData, JobTaskLog.class);
        taskLog.setExecTime(beginTime);
        try {
            Class<?> taskClazz = Class.forName(jobData.getClazzName());
            // 无参构造器
            Constructor<?> noArgConstructor = taskClazz.getConstructor();
            Object taskBean = noArgConstructor.newInstance();
            // 只允许调用 public 修改时的函数,其他修饰符的将会抛出异常
            // 执行定时任务目标函数
            String methodParams = jobData.getParams();
            if (null != methodParams) {
                Method taskMethod = taskClazz.getMethod(jobData.getMethodName(), String.class);
                returnValue = taskMethod.invoke(taskBean, methodParams);
            } else {
                Method taskMethod = taskClazz.getMethod(jobData.getMethodName());
                returnValue = taskMethod.invoke(taskBean);
            }
            logger.info(">>>> 执行定时任务：{} 成功 <<<<", jobData.getJobName());
        } catch (ClassNotFoundException e) {
            String message = String.format("Class定时任务反射获取类信息失败，请确认是否存在该类 class: %s", jobData.getClazzName());
            logger.error(message);
            throw new TaskException(message);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException |
                 NoSuchMethodException e) {
            String message = String.format("定时任务获取实例对象或获取函数失败,请确认类 %s 是否存在空参构造器,或没有对应参数的执行函数: %s",
                    jobData.getClazzName(), jobData.getMethodName());
            logger.error(message);
            throw new TaskException(message);
        } finally {
            // 记录日志
            this.taskLogRecords(trigger, jobData, taskLog);
        }
        if (null != returnValue) {
            logger.info("定时任务执返回数据 : {}", returnValue);
        }
        // 更新入库
        jobInfoService.updateById(jobData);
    }

}
