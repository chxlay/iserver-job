package com.iserver.cloud.job.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iserver.cloud.job.entity.JobTaskLog;
import com.iserver.cloud.job.service.TaskLogService;
import com.iserver.common.core.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * job日志记录
 *
 * @author Alay
 * @date 2022-07-15 09:37:32
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/logs")
@Api(value = "logs", tags = "job日志记录")
public class LogController {

    private final TaskLogService taskLogService;

    @GetMapping(value = "/page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    // @PreAuthorize("@pmh.hasPermission('job_log_view')")
    public R pageQuery(Page page, JobTaskLog jobTaskLog) {
        page = taskLogService.page(page, Wrappers.lambdaQuery(jobTaskLog));
        return R.ok(page);
    }


}
