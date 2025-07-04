/**
 * Copyright (c) 2019 人人开源 All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */
package io.boot.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* 订单
*
* @author epiphyllum.zhou@gmail.com
*/
@Mapper
public interface OrderDao extends BaseDao<OrderEntity> {
	
}