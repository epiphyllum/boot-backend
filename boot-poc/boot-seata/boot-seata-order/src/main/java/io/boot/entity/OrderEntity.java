/**
 * Copyright (c) 2019 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */
package io.boot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 订单
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Data
@Accessors(chain = true)
@TableName("seata_order")
public class OrderEntity {
	private static final long serialVersionUID = 1L;

	/**
	* id
	*/
	private Long id;
	/**
	* 商品编码
	*/
	private String commodityCode;
	/**
	* 数量
	*/
	private Integer count;
	/**
	* 金额
	*/
	private BigDecimal money;
}