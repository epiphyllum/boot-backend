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
import io.boot.commons.tools.validator.ValidatorUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;


/**
 * 订单
 *
 * @author epiphyllum.zhou@gmail.com
 */
@RestController
@RequestMapping("fake2")
@Tag(name = "订单")
public class Fake2Controller {

    @GetMapping("create")
    @Operation(summary = "创建订单")
    public Result create() {
        return new Result();
    }

    @GetMapping("i18n2")
    public String i18n2() {
        return MessageUtils.getMessage(100002002);
    }

    @GetMapping("i18n1")
    public String i18n1() {
        return MessageUtils.getMessage(403);
    }

    @GetMapping("ex")
    public Result ex() {
        throw new BootException(100002002);
    }

}