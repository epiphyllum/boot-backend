/**
 * Copyright (c) 2021 BootCloud All rights reserved.
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
 * 岗位管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_post")
public class SysPostEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	* 岗位编码
	*/
	private String postCode;
	/**
	* 岗位名称
	*/
	private String postName;
	/**
	* 排序
	*/
	private Integer sort;
	/**
	* 状态
	*/
	private Integer status;
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