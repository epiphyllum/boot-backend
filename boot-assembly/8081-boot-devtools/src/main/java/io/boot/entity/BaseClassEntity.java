/**
 * Copyright (c) 2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.boot.commons.tools.utils.DateUtils;
import lombok.Data;

import java.util.Date;

/**
 * 基类管理
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Data
@TableName("gen_base_class")
public class BaseClassEntity {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 基类包名
	 */
	private String packageName;
    /**
     * 基类编码
     */
	private String code;
    /**
     * 公共字段，多个用英文逗号分隔
     */
	private String fields;
    /**
     * 备注
     */
	private String remark;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date createDate;
}