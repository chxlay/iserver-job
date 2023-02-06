package com.iserver.cloud.job.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iserver.cloud.job.entity.JobInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务实体类管理
 *
 * @author Alay
 * @date 2021-10-31 19:44
 */
@Mapper
public interface JobInfoMapper extends BaseMapper<JobInfo> {

}
