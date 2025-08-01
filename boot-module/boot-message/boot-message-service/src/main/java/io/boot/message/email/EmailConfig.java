/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.message.email;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 邮件配置信息
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Data
@Schema(description = "邮件配置信息")
public class EmailConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "SMTP")
    @NotBlank(message = "{email.smtp.require}")
    private String smtp;

    @Schema(description = "端口号")
    @NotNull(message = "{email.port.require}")
    private Integer port;

    @Schema(description = "邮箱账号")
    @NotBlank(message = "{email.username.require}")
    private String username;

    @Schema(description = "邮箱密码")
    @NotBlank(message = "{email.password.require}")
    private String password;
}