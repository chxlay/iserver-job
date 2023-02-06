package com.iserver.cloud.job.invoker;

import cn.hutool.core.bean.BeanUtil;
import com.ejlchina.okhttps.HTTP;
import com.ejlchina.okhttps.HttpResult;
import com.ejlchina.okhttps.JacksonMsgConvertor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iserver.cloud.job.entity.JobInfo;
import com.iserver.cloud.job.entity.JobRestTemplate;
import com.iserver.cloud.job.entity.JobTaskLog;
import com.iserver.cloud.job.service.JobRestTemplateService;
import lombok.RequiredArgsConstructor;
import okhttp3.ConnectionPool;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Rest Url 调用执行的执行器
 *
 * @author Alay
 * @date 2021-10-31 18:01
 * @since
 */
@Service(value = "REST")
@RequiredArgsConstructor
public class RestTaskInvoker extends AbstractTaskInvoker {

    private final ObjectMapper objectMapper;
    private final JobRestTemplateService jobRestTemplateService;

    private HTTP http;

    /**
     * @return
     * @see { https://okhttps.ejlchina.com/v3/configuration.html }
     */
    private HTTP http() {
        if (null == http) {
            this.http = HTTP.builder()
                    // 配置连接池 最小10个连接（不配置默认为 5）
                    .config(builder -> builder.connectionPool(new ConnectionPool(10, 10, TimeUnit.MINUTES)))
                    .addMsgConvertor(new JacksonMsgConvertor(objectMapper))
                    .build();
        }
        return this.http;
    }

    @Override
    public void doInvoke(JobInfo jobData, Trigger trigger) {
        //任务开始时间
        long beginTime = System.currentTimeMillis();
        // 模拟数据库查询获取请求参数信息
        JobRestTemplate template = jobRestTemplateService.getByJobId(jobData.getJobId());
        JobTaskLog taskLog = BeanUtil.copyProperties(jobData, JobTaskLog.class);
        taskLog.setExecTime(beginTime);
        try {
            HttpResult httpResult = this.http().async(template.getExecPath())
                    .addHeader(template.getHeaders())
                    .bodyType(template.getBodyType())
                    .request(template.getMethod())
                    .getResult();
            if (httpResult.isSuccessful()) {
                logger.info("REST 调用成功");
            } else {
                logger.info("REST 调用失败");
                // 异常信息记录
                taskLog.setErrMessage("REST 调用失败");
                //设置执行失败信息
                taskLog.setSuccessful(false);
            }
        } catch (Exception e) {
            logger.error("#### 执行定时任务：{} 失败了, message: {},cause: {} ####", jobData.getJobName(), e.getMessage(), e.getCause());
            // 异常信息记录
            taskLog.setErrMessage(e.getMessage());
            // 设置执行失败信息
            taskLog.setSuccessful(false);
        } finally {
            // 日志记录
            this.taskLogRecords(trigger, jobData, taskLog);
        }
        // 更新入库
        jobInfoService.updateById(jobData);
    }

}