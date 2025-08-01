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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 新闻管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@Schema(description = "新闻管理")
public class NewsDTO implements Serializable {

    @Schema(description = "id")
    @Null(message = "{id.null}", groups = AddGroup.class)
    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    private Long id;

    @Schema(description = "标题")
    @NotBlank(message = "{news.title.require}", groups = DefaultGroup.class)
    private String title;

    @Schema(description = "内容")
    @NotBlank(message = "{news.content.require}", groups = DefaultGroup.class)
    private String content;

    @Schema(description = "发布时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date pubDate;

    @Schema(description = "创建时间")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date createDate;

}
