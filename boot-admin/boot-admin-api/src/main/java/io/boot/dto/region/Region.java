/**
 * Copyright (c) 2019 BootCloud All rights reserved.
 * <p>
 * https://www.boot.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.dto.region;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 地区管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@Schema(description = "地区管理")
public class Region implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "地区ID")
    private Long id;

    @JsonIgnore
    private Long pid;

    @Schema(description = "名称")
    private String name;
}
