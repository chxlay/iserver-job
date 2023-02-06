package com.iserver.cloud.job.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iserver.cloud.job.entity.JobRestTemplate;
import com.iserver.cloud.job.mapper.JobRestTemplateMapper;
import com.iserver.cloud.job.service.JobRestTemplateService;
import com.iserver.common.core.exception.UniqueException;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 任务Rest请求模板管理
 *
 * @author Alay
 * @date 2021-12-31 16:43:48
 */
@Service
public class JobRestTemplateServiceImpl extends ServiceImpl<JobRestTemplateMapper, JobRestTemplate> implements JobRestTemplateService {


    @Override
    public void uniqueCheck(JobRestTemplate restInfo) {
        List<JobRestTemplate> templates = this.list();
        if (templates.isEmpty()) return;
        for (JobRestTemplate template : templates) {
            if (template.equalsAndExcludeSelf(restInfo)) {
                throw new UniqueException();
            }
        }
    }

    @Override
    public void upsertInit(JobRestTemplate restInfo) {
        // 暂时没有实现逻辑需求
    }

    @Override
    public JobRestTemplate getByJobId(Integer jobId) {
        return this.getOne(Wrappers.<JobRestTemplate>lambdaQuery().eq(JobRestTemplate::getJobId, jobId));
    }
}
