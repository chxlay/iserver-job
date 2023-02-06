package com.iserver.cloud.job.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iserver.cloud.job.entity.JobRestTemplate;
import org.apache.ibatis.annotations.Mapper;


/**
 * 任务Rest请求模板管理
 *
 * @author Alay
 * @date 2021-12-31 16:43:48
 */
@Mapper
public interface JobRestTemplateMapper extends BaseMapper<JobRestTemplate> {

}
