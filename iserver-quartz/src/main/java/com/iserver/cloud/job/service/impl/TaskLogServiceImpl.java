package com.iserver.cloud.job.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iserver.cloud.job.entity.JobTaskLog;
import com.iserver.cloud.job.mapper.TaskLogMapper;
import com.iserver.cloud.job.service.TaskLogService;
import org.springframework.stereotype.Service;


/**
 * @author zhuzi
 * @date 2022-07-15 09:37:32
 */
@Service
public class TaskLogServiceImpl extends ServiceImpl<TaskLogMapper, JobTaskLog> implements TaskLogService {

}
