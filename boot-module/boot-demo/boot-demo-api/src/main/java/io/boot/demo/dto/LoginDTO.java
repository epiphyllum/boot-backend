/**
 * /**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录表单
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Data
@Schema(description = "登录表单")
public class LoginDTO {
    @Schema(description = "手机号")
    @NotBlank(message = "{api.mobile.require}")
    private String mobile;

    @Schema(description = "密码")
    @NotBlank(message = "{api.password.require}")
    private String password;

}