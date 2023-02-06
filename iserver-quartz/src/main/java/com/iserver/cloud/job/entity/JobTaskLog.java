package com.iserver.cloud.job.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


/**
 * 任务调度日志记录
 *
 * @author Alay
 * @date 2022-07-15 09:37:32
 */
@Getter
@Setter
@TableName("job_task_log")
@ApiModel(value = "任务调度日志记录")
@EqualsAndHashCode(callSuper = true)
public class JobTaskLog extends Model<JobTaskLog> {
    private static final long serialVersionUID = 1L;
    /**
     * 任务日志id
     */
    @TableId
    @ApiModelProperty(value = "任务日志id")
    private String id;
    /**
     * 任务id
     */
    @ApiModelProperty(value = "任务id")
    private Integer jobId;
    /**
     * 执行状态
     */
    @ApiModelProperty(value = "执行状态")
    private Boolean successful;
    /**
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称")
    private String jobName;
    /**
     * 任务组名
     */
    @ApiModelProperty(value = "任务组名")
    private String jobGroup;
    /**
     * 任务类名(IOC)
     */
    @ApiModelProperty(value = "任务类名(IOC)")
    private String taskClazzName;
    /**
     * 执行方法
     */
    @ApiModelProperty(value = "执行方法")
    private String methodName;
    /**
     * 执行参数
     */
    @ApiModelProperty(value = "执行参数")
    private String params;

    /**
     * 执行信息
     */
    @ApiModelProperty(value = "执行信息")
    private String invokerMessage;
    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误信息")
    private String errMessage;
    /**
     * cron执行表达式
     */
    @ApiModelProperty(value = "cron执行表达式")
    private String cronExpression;
    /**
     * 租户Id
     */
    @ApiModelProperty(value = "租户Id", hidden = true)
    private String tenantId;
    /**
     * 逻辑删除
     */
    @JsonIgnore
    @TableLogic
    @TableField(select = false)
    @ApiModelProperty(value = "逻辑删除", hidden = true)
    private Boolean isDelete;
    /**
     * 执行时长
     */
    @ApiModelProperty(value = "执行时长")
    private Long invokerDuration;
    /**
     * 执行时间戳
     */
    @ApiModelProperty(value = "执行时间戳")
    private Long execTime;

}
