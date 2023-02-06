package com.iserver.cloud.job.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iserver.cloud.job.enums.JobStateEnum;
import com.iserver.cloud.job.enums.JobTypeEnum;
import com.iserver.common.job.enums.MisfireEnum;
import com.iserver.common.job.extension.IQuartzJobData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 定时任务实体类
 *
 * @author Alay
 * @date 2021-10-31 17:07
 */
@Getter
@Setter
@ApiModel(value = "定时任务实体类")
@TableName(value = "job_info")
public class JobInfo extends Model<JobInfo> implements IQuartzJobData {
    private static final long serialVersionUID = 1L;
    /**
     * JobId 任务Id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "JobId 任务Id")
    private Integer jobId;
    /**
     * 任务名称
     */
    @NotBlank(message = "任务名不能空")
    @ApiModelProperty(value = "任务名称")
    private String jobName;
    /**
     * 任务组名
     */
    @ApiModelProperty(value = "任务分组")
    private String jobGroup;
    /**
     * 任务类名
     */
    @NotBlank(message = "任务类名不能空")
    @ApiModelProperty(value = "任务类名")
    private String clazzName;
    /**
     * 任务方法名称
     */
    @NotBlank(message = "方法名不能空")
    @ApiModelProperty(value = "方法名")
    private String methodName;
    /**
     * 任务类型(1:定时任务 2:周期任务)
     */
    @ApiModelProperty(value = "任务类型")
    private JobTypeEnum jobType;
    /**
     * 执行参数值
     */
    @ApiModelProperty(value = "执行参数")
    private String params;
    /**
     * cron表达式
     */
    @NotBlank(message = "cron不能空")
    @ApiModelProperty(value = "cron表达式")
    private String cronExpression;
    /**
     * 错失执行策略
     *
     * @see MisfireEnum
     * 0: 默认策略,不执行
     * 1: 错失周期立即执行
     * 2: 下周期执行
     * 3: 什么也不做,不做执行
     */
    @ApiModelProperty(value = "错失执行策略")
    private Integer misfirePolicy;
    /**
     * 任务状态:1-未发布,2-暂停,3-运行中,4-移除
     */
    @ApiModelProperty(value = "任务状态:1-未发布,2-暂停,3-运行中,4-移除")
    private JobStateEnum jobState;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String description;
    /**
     * 首次执行时间
     */
    @ApiModelProperty(value = "首次执行时间")
    private LocalDateTime firstStart;
    /**
     * 上次执行时间
     */
    @ApiModelProperty(value = "上次执行时间")
    private LocalDateTime lastTime;
    /**
     * 下次执行时间
     */
    @ApiModelProperty(value = "下次执行时间")
    private LocalDateTime nextTime;
    /**
     * 租户Id
     */
    @ApiModelProperty(value = "租户Id", hidden = true)
    private String tenantId;
    /**
     * 逻辑删除
     */
    @TableLogic
    @JsonIgnore
    @TableField(select = false)
    @ApiModelProperty(value = "逻辑删除", hidden = true)
    private Boolean isDelete;
    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "创建者")
    private String upsertBy;
    /**
     * 插入/更新时间
     */
    @ApiModelProperty(value = "插入/更新时间", hidden = true)
    private LocalDateTime upsertTime;

    @Override
    public String name() {
        return jobId.toString();
    }

    @Override
    public String group() {
        return jobGroup;
    }

    @Override
    public String type() {
        return jobType.name();
    }

    @Override
    public String cronExpression() {
        return cronExpression;
    }

    @Override
    public String desc() {
        return description;
    }

    @Override
    public Integer misfire() {
        return misfirePolicy;
    }
}
