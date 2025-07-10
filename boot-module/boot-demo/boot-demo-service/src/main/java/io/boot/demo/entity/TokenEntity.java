/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户Token
 * 
 * @author epiphyllum.zhou@gmail.com
 */
@Data
@TableName("tb_token")
public class TokenEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;
	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 用户token
	 */
	private String token;
	/**
	 * 过期时间
	 */
	private Date expireDate;
	/**
	 * 更新时间
	 */
	private Date updateDate;

}