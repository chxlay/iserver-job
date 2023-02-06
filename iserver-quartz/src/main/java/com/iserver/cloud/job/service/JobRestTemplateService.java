package com.iserver.cloud.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iserver.cloud.job.entity.JobRestTemplate;


/**
 * 任务Rest请求模板管理
 *
 * @author Alay
 * @date 2021-12-31 16:43:48
 */
public interface JobRestTemplateService extends IService<JobRestTemplate> {
    /**
     * 数据唯一性校验
     *
     * @param restInfo
     */
    void uniqueCheck(JobRestTemplate restInfo);

    /**
     * 更新/插入 时初始化
     *
     * @param restInfo
     */
    void upsertInit(JobRestTemplate restInfo);

    /**
     * 根据任务查询
     *
     * @param jobId
     * @return
     */
    JobRestTemplate getByJobId(Integer jobId);

}
