/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.boot.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.rbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 修改密码
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Data
@Schema(description = "修改密码")
public class PasswordDTO implements Serializable {
    private static final long serialVersionUID = 1L;
 
    @Schema(description = "原密码")
    @NotBlank(message = "{sysuser.password.require}")
    private String password;

    @Schema(description = "新密码")
    @NotBlank(message = "{sysuser.password.require}")
    private String newPassword;

}