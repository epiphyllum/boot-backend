/**
 * Copyright (c) 2021 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.payment.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.payment.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
* 订单
*
* @author epiphyllum.zhou@gmail.com
*/
@Mapper
public interface OrderDao extends BaseDao<OrderEntity> {

    /**
     * 支付成功
     */
    int paySuccess(@Param("orderId") Long orderId, @Param("status") Integer status, @Param("payAt") Date payAt);

    OrderEntity getByOrderId(Long orderId);
}