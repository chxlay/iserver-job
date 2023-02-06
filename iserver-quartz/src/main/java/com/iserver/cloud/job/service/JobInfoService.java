package com.iserver.cloud.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iserver.cloud.job.entity.JobInfo;

import java.util.List;

/**
 * 定时任务实体类管理
 *
 * @author Alay
 * @date 2021-10-31 19:42
 */
public interface JobInfoService extends IService<JobInfo> {

    /**
     * 查询所有已启动中的数据
     *
     * @return
     */
    List<JobInfo> listStarted();

}
