/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.boot.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.boot.commons.tools.utils.DateUtils;
import io.boot.commons.tools.validator.group.AddGroup;
import io.boot.commons.tools.validator.group.DefaultGroup;
import io.boot.commons.tools.validator.group.UpdateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典类型
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@Schema(description = "字典类型")
public class SysDictTypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;
 
    @Schema(description = "id")
    @Null(message = "{id.null}", groups = AddGroup.class)
    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    private Long id;

    @Schema(description = "字典类型")
    @NotBlank(message = "{sysdict.type.require}", groups = DefaultGroup.class)
    private String dictType;

    @Schema(description = "字典名称")
    @NotBlank(message = "{sysdict.name.require}", groups = DefaultGroup.class)
    private String dictName;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "排序")
    @Min(value = 0, message = "{sort.number}", groups = DefaultGroup.class)
    private Integer sort;

    @Schema(description = "创建时间")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date createDate;

    @Schema(description = "更新时间")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date updateDate;
}