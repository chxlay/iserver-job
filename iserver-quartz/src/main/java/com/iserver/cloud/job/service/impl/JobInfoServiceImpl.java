package com.iserver.cloud.job.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iserver.cloud.job.entity.JobInfo;
import com.iserver.cloud.job.enums.JobStateEnum;
import com.iserver.cloud.job.mapper.JobInfoMapper;
import com.iserver.cloud.job.service.JobInfoService;
import com.iserver.common.job.utils.IQuartzUtil;
import lombok.RequiredArgsConstructor;
import org.quartz.Scheduler;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务实体类管理
 *
 * @author Alay
 * @date 2021-10-31 19:43
 * @since
 */
@Service
@RequiredArgsConstructor
public class JobInfoServiceImpl extends ServiceImpl<JobInfoMapper, JobInfo> implements JobInfoService {

    private final Scheduler scheduler;

    @Override
    public List<JobInfo> listStarted() {
        return this.list(Wrappers.<JobInfo>lambdaQuery().eq(JobInfo::getJobState, JobStateEnum.RUNNING));
    }

    /**
     * 启动时初始化启动中的任务到 Quartz
     */
    @Order
    @EventListener({WebServerInitializedEvent.class})
    public void initJob() {
        // 所有已启动中的定时任务
        List<JobInfo> jobInfos = this.listStarted();
        if (jobInfos.isEmpty()) return;

        // 运行中的任务添加到 Quartz
        for (JobInfo job : jobInfos) {
            boolean exists = IQuartzUtil.checkExists(job, scheduler);
            if (exists) {
                // 删除原来的任务,重新添加
                IQuartzUtil.removeJob(job, scheduler);
            }
            // 添加运行中的定时任务
            IQuartzUtil.addNewJob(job, scheduler);
        }
    }

}
