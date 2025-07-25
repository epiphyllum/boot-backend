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
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.boot.commons.tools.utils.DateUtils;
import lombok.Data;

import java.util.Date;

/**
 * 字段类型管理
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Data
@TableName("gen_field_type")
public class FieldTypeEntity {
	/**
	 * id
	 */
	@TableId
	private Long id;
    /**
     * 字段类型
     */
	private String columnType;
    /**
     * 属性类型
     */
	private String attrType;
	/**
	 * 属性包名
	 */
	private String packageName;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date createDate;
}