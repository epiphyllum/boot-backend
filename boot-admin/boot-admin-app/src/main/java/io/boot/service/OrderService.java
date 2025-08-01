/**
 * Copyright (c) 2021 BootCloud All rights reserved.
 *
 * https://www.boot.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.service;

import io.boot.commons.mybatis.service.CrudService;
import io.boot.dto.OrderDTO;
import io.boot.entity.OrderEntity;

/**
 * 订单
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface OrderService extends CrudService<OrderEntity, OrderDTO> {


    OrderEntity getByOrderId(Long orderId);

    /**
     * 支付成功
     * @param order 订单
     */
    void paySuccess(OrderEntity order);
}