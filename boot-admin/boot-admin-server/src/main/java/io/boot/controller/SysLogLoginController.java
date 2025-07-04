/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.controller;

import io.boot.commons.log.annotation.LogOperation;
import io.boot.commons.tools.constant.Constant;
import io.boot.commons.tools.page.PageData;
import io.boot.commons.tools.utils.ExcelUtils;
import io.boot.commons.tools.utils.Result;
import io.boot.dto.SysLogLoginDTO;
import io.boot.excel.SysLogLoginExcel;
import io.boot.service.SysLogLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 登录日志
 *
 * @author epiphyllum.zhou@gmail.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("log/login")
@Tag(name = "登录日志")
public class SysLogLoginController {
    @Resource
    private SysLogLoginService sysLogLoginService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @Parameters({
            @Parameter(name = Constant.PAGE, description = "当前页码，从1开始", required = true),
            @Parameter(name = Constant.LIMIT, description = "每页显示记录数", required = true),
            @Parameter(name = Constant.ORDER_FIELD, description = "排序字段"),
            @Parameter(name = Constant.ORDER, description = "排序方式，可选值(asc、desc)"),
            @Parameter(name = "status", description = "状态  0：失败    1：成功    2：账号已锁定"),
            @Parameter(name = "creatorName", description = "用户名")
    })
    @PreAuthorize("hasAuthority('sys:log:login')")
    public Result<PageData<SysLogLoginDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        PageData<SysLogLoginDTO> page = sysLogLoginService.page(params);

        return new Result<PageData<SysLogLoginDTO>>().ok(page);
    }

    @GetMapping("export")
    @Operation(summary = "导出")
    @LogOperation("Export Log Login")
    @Parameters({
            @Parameter(name = "status", description = "状态  0：失败    1：成功    2：账号已锁定"),
            @Parameter(name = "creatorName", description = "用户名")
    })
    @PreAuthorize("hasAuthority('sys:log:login')")
    public void export(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<SysLogLoginDTO> list = sysLogLoginService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "登录日志", list, SysLogLoginExcel.class);
    }

}