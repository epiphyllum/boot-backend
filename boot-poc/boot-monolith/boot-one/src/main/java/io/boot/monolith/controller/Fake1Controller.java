/**
 * Copyright (c) 2019 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.boot.monolith.controller;

import io.boot.commons.tools.exception.BootException;
import io.boot.commons.tools.utils.MessageUtils;
import io.boot.commons.tools.utils.Result;
import io.boot.commons.tools.validator.ValidatorUtils;
import io.boot.commons.tools.validator.group.AddGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;


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

    @GetMapping("create")
    @Operation(summary = "创建订单")
    public Result create() {
        return new Result();
    }

    @GetMapping("i18n")
    public String i18n() {
        return MessageUtils.getMessage(100001001);
    }

    public static class DemoDTO implements Serializable {
        private static final long serialVersionUID = 1L;
        @NotNull(message = "{mail.name.require}", groups = AddGroup.class)
        private Long id;
    }

    @GetMapping("validation1")
    public Result validation1() {
        DemoDTO dto = new DemoDTO();
        ValidatorUtils.validateEntity(dto, AddGroup.class);
        return new Result();
    }

    @GetMapping("validation2")
    public String validation2() {
        DemoDTO dto = new DemoDTO();
        ValidatorUtils.validateEntity(dto, AddGroup.class);
        return "ok";
    }

    @GetMapping("bootEx")
    public String bootEx() {
        throw new BootException("bootEx");
    }

}