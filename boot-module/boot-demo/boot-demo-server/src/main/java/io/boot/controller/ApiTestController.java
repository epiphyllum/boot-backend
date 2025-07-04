/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.controller;

import io.boot.annotation.Login;
import io.boot.annotation.LoginUser;
import io.boot.commons.tools.utils.Result;
import io.boot.dto.UploadDTO;
import io.boot.entity.UserEntity;
import io.boot.feign.OssFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 测试接口
 *
 * @author epiphyllum.zhou@gmail.com
 */
@RestController
@RequestMapping("test")
@Tag(name = "测试接口")
public class ApiTestController {
    @Resource
    private OssFeignClient ossFeignClient;

    @Login
    @GetMapping("userInfo")
    public Result<UserEntity> userInfo(@Parameter(hidden = true) @LoginUser UserEntity user) {
        return new Result<UserEntity>().ok(user);
    }

    @Login
    @GetMapping("userId")
    @Operation(summary = "获取用户ID")
    public Result<Long> userInfo(@Parameter(hidden = true) @RequestAttribute("userId") Long userId) {
        return new Result<Long>().ok(userId);
    }

    @GetMapping("notToken")
    @Operation(summary = "忽略Token验证测试")
    public Result<String> notToken() {
        return new Result<String>().ok("无需token也能访问。。。");
    }

    @PostMapping("upload")
    public Result<UploadDTO> upload(@RequestParam("file") MultipartFile file) {
        Result<UploadDTO> dto = ossFeignClient.upload(file);

        return dto;
    }
}