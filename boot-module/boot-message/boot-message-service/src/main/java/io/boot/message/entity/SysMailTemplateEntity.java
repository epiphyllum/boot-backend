/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.message.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.boot.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮件模板
 * 
 * @author epiphyllum.zhou@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_mail_template")
public class SysMailTemplateEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 模板名称
	 */
	private String name;
	/**
	 * 邮件主题
	 */
	private String subject;
	/**
	 * 邮件正文
	 */
	private String content;

}