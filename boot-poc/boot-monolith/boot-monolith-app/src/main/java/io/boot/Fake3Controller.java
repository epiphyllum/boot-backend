/**
 * Copyright (c) 2019 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.boot;

import io.boot.commons.tools.exception.BootException;
import io.boot.commons.tools.utils.MessageUtils;
import io.boot.commons.tools.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 订单
 *
 * @author epiphyllum.zhou@gmail.com
 */
@RestController
@RequestMapping("fake3")
@Tag(name = "订单")
public class Fake3Controller {

    @GetMapping("create")
    @Operation(summary = "创建订单")
    public Result create() {
        return  new Result();
    }

    @GetMapping("i18n")
    public String i18n() {
        return MessageUtils.getMessage(403);
    }
}