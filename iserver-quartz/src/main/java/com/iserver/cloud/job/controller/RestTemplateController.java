package com.iserver.cloud.job.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iserver.cloud.job.entity.JobRestTemplate;
import com.iserver.cloud.job.service.JobRestTemplateService;
import com.iserver.common.core.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 任务Rest请求模板管理
 *
 * @author Alay
 * @date 2021-12-31 16:43:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/template")
@Api(value = "template", tags = "任务Rest请求模板管理")
public class RestTemplateController {

    private final JobRestTemplateService jobRestTemplateService;


    @GetMapping(value = "/page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    // @PreAuthorize("@pmh.hasPermission('job_template_view')")
    public R pageQuery(Page page, JobRestTemplate restInfo) {
        page = jobRestTemplateService.page(page, Wrappers.lambdaQuery(restInfo));
        return R.ok(page);
    }


    // @SysLog("新增/修改Job参数模板")
    @PutMapping(value = "/upsert")
    @ApiOperation(value = "新增/修改Job参数模板", notes = "新增/修改Job参数模板")
    // @PreAuthorize("@pmh.hasPermission('job_template_add')")
    public R upsert(@RequestBody JobRestTemplate restInfo) {
        // 数据唯一性校验
        jobRestTemplateService.uniqueCheck(restInfo);
        // 数据初始化或校验
        jobRestTemplateService.upsertInit(restInfo);
        // 入库
        boolean isOk = null == restInfo.getId() ? jobRestTemplateService.updateById(restInfo) : jobRestTemplateService.save(restInfo);
        return R.ok(isOk);
    }


    // @SysLog("删除Rest模板")
    @DeleteMapping(value = "/remove/{id}")
    // @PreAuthorize("@pmh.hasPermission('job_template_del')")
    @ApiOperation(value = "删除Rest模板", notes = "删除Rest模板")
    public R removeById(@PathVariable Integer id) {
        boolean isOk = jobRestTemplateService.removeById(id);
        return R.ok(isOk);
    }

}
