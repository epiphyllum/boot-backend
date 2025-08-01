/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.message.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.boot.commons.tools.utils.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Data
@Schema(description = "短信")
public class SysSmsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "短信编码")
    private String smsCode;

    @Schema(description = "平台类型")
    private Integer platform;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "短信配置")
    private SmsConfig config;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date createDate;

}
