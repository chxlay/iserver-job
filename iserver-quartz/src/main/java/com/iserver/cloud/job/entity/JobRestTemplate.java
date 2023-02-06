package com.iserver.cloud.job.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iserver.cloud.job.typehandler.MapTypeHandler;
import com.iserver.common.core.lang.UniqueEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;


/**
 * 任务Rest请求模板信息
 *
 * @author Alay
 * @date 2021-12-31 16:43:48
 */
@Data
@TableName("job_rest_template")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "任务Rest请求模板管理")
public class JobRestTemplate extends Model<JobRestTemplate> implements UniqueEntity<JobRestTemplate> {
    private static final long serialVersionUID = 1L;
    /**
     * Rest请求方式Id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "Rest请求方式Id", hidden = true)
    private Integer id;
    /**
     * 任务ID
     */
    @ApiModelProperty(value = "任务ID")
    private Integer jobId;
    /**
     * 执行路径
     */
    @ApiModelProperty(value = "执行路径")
    private String execPath;
    /**
     * 请求方式
     */
    @ApiModelProperty(value = "请求方式")
    private String method;
    /**
     * 请求参数类型
     */
    @ApiModelProperty(value = "请求参数类型")
    private String bodyType;
    /**
     * 请求头信息
     */
    @ApiModelProperty(value = "请求头信息")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, String> headers;
    /**
     * 额外参数
     */
    @ApiModelProperty(value = "额外参数")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, String> parameters;
    /**
     * 发布版本
     */
    @ApiModelProperty(value = "发布版本")
    private String sinceVersion;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
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
     * 操作者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "操作者", hidden = true)
    private String upsertBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间", hidden = true)
    private LocalDateTime upsertTime;

    @Override
    public boolean isEquals(JobRestTemplate source) {
        return  Objects.equals(this.jobId, source.jobId);
    }

    @Override
    public boolean isSelf(JobRestTemplate source) {
        return Objects.equals(this.id, source.id);
    }
}
