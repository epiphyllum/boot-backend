/**
 * Copyright (c) 2021 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.payment.controller;


import io.boot.commons.tools.constant.Constant;
import io.boot.commons.tools.page.PageData;
import io.boot.commons.tools.utils.Result;
import io.boot.payment.dto.WeChatNotifyLogDTO;
import io.boot.payment.service.WeChatNotifyLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * 微信支付回调日志
 *
 * @author epiphyllum.zhou@gmail.com
 */
@AllArgsConstructor
@RestController
@RequestMapping("pay/wechatNotifyLog")
public class WeChatNotifyLogController {
    private final WeChatNotifyLogService weChatNotifyLogService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @Parameters({
            @Parameter(name = Constant.PAGE, description = "当前页码，从1开始", required = true),
            @Parameter(name = Constant.LIMIT, description = "每页显示记录数", required = true),
            @Parameter(name = Constant.ORDER_FIELD, description = "排序字段"),
            @Parameter(name = Constant.ORDER, description = "排序方式，可选值(asc、desc)")
    })
    @PreAuthorize("hasAuthority('pay:wechatNotifyLog:all')")
    public Result<PageData<WeChatNotifyLogDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params) {
        PageData<WeChatNotifyLogDTO> page = weChatNotifyLogService.page(params);

        return new Result<PageData<WeChatNotifyLogDTO>>().ok(page);
    }
}