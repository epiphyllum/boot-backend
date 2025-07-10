/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.boot.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.rbac.controller;

import io.boot.commons.log.annotation.LogOperation;
import io.boot.commons.tools.constant.Constant;
import io.boot.commons.tools.page.PageData;
import io.boot.commons.tools.utils.ExcelUtils;
import io.boot.commons.tools.utils.Result;
import io.boot.rbac.dto.SysLogErrorDTO;
import io.boot.rbac.excel.SysLogErrorExcel;
import io.boot.rbac.service.SysLogErrorService;
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
 * 异常日志
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("log/error")
@Tag(name = "异常日志")
public class SysLogErrorController {
    @Resource
    private SysLogErrorService sysLogErrorService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @Parameters({
            @Parameter(name = Constant.PAGE, description = "当前页码，从1开始", required = true),
            @Parameter(name = Constant.LIMIT, description = "每页显示记录数", required = true),
            @Parameter(name = Constant.ORDER_FIELD, description = "排序字段"),
            @Parameter(name = Constant.ORDER, description = "排序方式，可选值(asc、desc)"),
            @Parameter(name = "module", description = "模块名称，如：sys")
    })
    @PreAuthorize("hasAuthority('sys:log:error')")
    public Result<PageData<SysLogErrorDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        PageData<SysLogErrorDTO> page = sysLogErrorService.page(params);

        return new Result<PageData<SysLogErrorDTO>>().ok(page);
    }

    @GetMapping("export")
    @Operation(summary = "导出")
    @LogOperation("Export Log Error")
    @PreAuthorize("hasAuthority('sys:log:error')")
    public void export(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<SysLogErrorDTO> list = sysLogErrorService.list(params);

        ExcelUtils.exportExcelToTarget(response, null, "异常日志", list, SysLogErrorExcel.class);
    }

}