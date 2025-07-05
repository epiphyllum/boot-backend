/**
 * Copyright (c) 2019 人人开源 All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.boot.monolith.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.boot.commons.tools.exception.BootException;
import io.boot.commons.tools.utils.MessageUtils;
import io.boot.commons.tools.utils.Result;
import io.boot.monolith.MonoClient;
import io.boot.monolith.dto.DemoData;
import io.boot.monolith.dto.DemoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 订单
 *
 * @author epiphyllum.zhou@gmail.com
 */
@RestController
@RequestMapping("fake1")
@Tag(name = "订单")
@Slf4j
public class Fake1Controller {

    @Resource
    private MonoClient monoClient;

    @GetMapping("create")
    @Operation(summary = "创建订单")
    public Result create() {
        return new Result();
    }

    @GetMapping("info")
    public Result<DemoData> info() throws JsonProcessingException {
        DemoRequest demoRequest = new DemoRequest();
        demoRequest.setId(1);
        Result<DemoData> demoDataResult = monoClient.demoInfo(demoRequest);
        return demoDataResult;
    }

    @GetMapping("i18n")
    public String i18n() {
        return MessageUtils.getMessage(100001001);
    }

}