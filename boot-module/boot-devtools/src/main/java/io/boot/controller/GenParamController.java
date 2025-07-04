/**
 * Copyright (c) 2020 人人开源 All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.controller;

import io.boot.commons.tools.utils.JsonUtils;
import io.boot.commons.tools.utils.Result;
import io.boot.entity.GenParam;
import io.boot.remote.ParamsRemoteService;
import io.boot.utils.ModuleConstant;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 代码生成参数配置
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("param")
public class GenParamController {
    @Resource
    private ParamsRemoteService paramsRemoteService;

    @GetMapping("info")
    public Result<GenParam> info() {
        GenParam param = paramsRemoteService.getValueObject(ModuleConstant.DEV_TOOLS_PARAM_KEY, GenParam.class);

        return new Result<GenParam>().ok(param);
    }

    @PostMapping
    public Result saveConfig(@RequestBody GenParam param) {
        paramsRemoteService.updateValueByCode(ModuleConstant.DEV_TOOLS_PARAM_KEY, JsonUtils.toJsonString(param));

        return new Result();
    }
}