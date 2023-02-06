package com.iserver.cloud.job.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iserver.cloud.job.entity.JobTaskLog;
import org.apache.ibatis.annotations.Mapper;


/**
 * 任务调度日志记录
 *
 * @author Alay
 * @date 2022-07-15 09:37:32
 */
@Mapper
public interface TaskLogMapper extends BaseMapper<JobTaskLog> {

}
