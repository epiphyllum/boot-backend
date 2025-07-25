/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.boot.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.boot.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 参数管理
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_params")
public class SysParamsEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 参数编码
	 */
	private String paramCode;
	/**
	 * 参数值
	 */
	private String paramValue;
	/**
	 * 类型   0：系统参数   1：非系统参数
	 */
	private Integer paramType;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 删除标识  0：未删除    1：删除
	 */
	@TableField(fill = FieldFill.INSERT)
	private Integer delFlag;
	/**
	 * 更新者
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;
	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;

}