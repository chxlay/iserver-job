package com.iserver.cloud.job.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iserver.cloud.job.entity.JobInfo;
import com.iserver.cloud.job.enums.JobStateEnum;
import com.iserver.cloud.job.service.JobInfoService;
import com.iserver.common.core.result.R;
import com.iserver.common.job.utils.IQuartzUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.quartz.Scheduler;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 定时任务管理
 *
 * @author Alay
 * @date 2021-10-31 18:30
 * @see <a href=" https://www.cnblogs.com/Alay/p/15488175.html">cron表达式</a>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/task")
@Api(value = "task", tags = "定时任务管理")
public class TaskController {

    private final Scheduler scheduler;
    private final JobInfoService jobInfoService;

    @GetMapping(value = "/page")
    @ApiOperation(value = "分页查询定时任务", notes = "分页查询定时任务")
    // @PreAuthorize("@pmh.hasPermission('job_task_view')")
    public R page(Page page, JobInfo jobInfo) {
        page = jobInfoService.page(page, Wrappers.lambdaQuery(jobInfo));
        return R.ok(page);
    }


    // @SysLog("添加定时任务")
    @PostMapping(value = "/addJob")
    @ApiOperation(value = "添加定时任务", notes = "添加定时任务")
    // @PreAuthorize("@pmh.hasPermission('job_task_add')")
    public R addJob(@RequestBody @Valid JobInfo jobInfo) {
        // 这里示例代码没有对前端的参数进行校验,实际开发中需要对参数进行校验
        boolean isOk = jobInfoService.save(jobInfo);
        // 新增的定时任务为直接运行状态
        if (isOk && JobStateEnum.RUNNING == jobInfo.getJobState()) {
            // 再次重数据库中获取出 任务数据（部分数据未传参,使用数据库默认值）
            jobInfo = jobInfoService.getById(jobInfo.getJobId());
            IQuartzUtil.addNewJob(jobInfo, scheduler);
        }
        return R.ok(isOk);
    }


    // @SysLog("修改定时任务")
    @PutMapping(value = "/updateJob")
    @ApiOperation(value = "修改定时任务", notes = "修改定时任务")
    // @PreAuthorize("@pmh.hasPermission('job_task_edit')")
    public R updateJob(@RequestBody @Valid JobInfo jobInfo) {
        JobInfo jobInDb = jobInfoService.getById(jobInfo.getJobId());
        boolean isOk = jobInfoService.updateById(jobInfo);
        // 移除定时任务
        IQuartzUtil.removeJob(jobInfo, scheduler);
        // 新增的定时任务为运行状态时,需要暂停重新运行
        if (JobStateEnum.RUNNING == jobInDb.getJobState()) {
            // 添加新的定时任务
            IQuartzUtil.addNewJob(jobInfo, scheduler);
        }
        return R.ok(isOk);
    }


    // @SysLog("暂停定时任务")
    @PutMapping(value = "/pauseJob/{jobId}")
    @ApiOperation(value = "暂停定时任务", notes = "暂停定时任务")
    // @PreAuthorize("@pmh.hasPermission('job_task_edit')")
    public R pauseJob(@PathVariable(value = "jobId") Integer jobId) {
        JobInfo jobInfo = jobInfoService.getById(jobId);
        // 暂停任务
        IQuartzUtil.pauseJob(jobInfo, scheduler);
        // 修改数据库
        jobInfo.setJobState(JobStateEnum.PAUSE);
        boolean isOk = jobInfoService.updateById(jobInfo);
        return R.ok(isOk);
    }


    // @SysLog("恢复定时任务")
    @PutMapping(value = "/resumeJob/{jobId}")
    @ApiOperation(value = "恢复定时任务", notes = "恢复定时任务")
    // @PreAuthorize("@pmh.hasPermission('job_task_edit')")
    public R resumeJob(@PathVariable(value = "jobId") Integer jobId) {
        JobInfo jobInfo = jobInfoService.getById(jobId);
        if (JobStateEnum.RUNNING.le(jobInfo.getJobState())) {
            // 实际项目中返回数据数据体使用常量,这里简单示例
            return R.failed("ERR0001", "当前状态不符合");
        }
        boolean exists = IQuartzUtil.checkExists(jobInfo, scheduler);
        if (!exists) {
            IQuartzUtil.addNewJob(jobInfo, scheduler);
        }
        // 恢复任务
        IQuartzUtil.resumeJob(jobInfo, scheduler);

        // 修改数据库
        jobInfo.setJobState(JobStateEnum.RUNNING);
        boolean isOk = jobInfoService.updateById(jobInfo);
        return R.ok(isOk);
    }


    @PostMapping("/runJob/{id}")
    @ApiOperation(value = "立刻执行定时任务")
    // @PreAuthorize("@pmh.hasPermission('job_task_edit')")
    public R runJob(@PathVariable("id") Integer jobId) {
        JobInfo jobInfo = this.jobInfoService.getById(jobId);
        boolean exists = IQuartzUtil.checkExists(jobInfo, scheduler);
        if (!exists) {
            IQuartzUtil.addNewJob(jobInfo, scheduler);
        }
        boolean isOk = IQuartzUtil.runJob(jobInfo, scheduler);
        return isOk ? R.ok() : R.failed();
    }


    // @SysLog("移除定时任务")
    @DeleteMapping(value = "/removeJob/{jobId}")
    @ApiOperation(value = "移除定时任务", notes = "移除定时任务")
    // @PreAuthorize("@pmh.hasPermission('job_task_del')")
    public R removeJob(@PathVariable(value = "jobId") Integer jobId) {
        JobInfo jobInfo = jobInfoService.getById(jobId);
        // 运行中的任务需要移除执行
        if (JobStateEnum.RUNNING == jobInfo.getJobState()) {
            // 运行中的任务需要先移除
            IQuartzUtil.removeJob(jobInfo, scheduler);
        }
        jobInfo.setJobState(JobStateEnum.REMOVED);
        boolean isOk = jobInfoService.updateById(jobInfo);
        return R.ok(isOk);
    }


}
