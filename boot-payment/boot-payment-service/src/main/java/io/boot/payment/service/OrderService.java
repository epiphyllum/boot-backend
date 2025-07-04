/**
 * Copyright (c) 2021 人人开源 All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.payment.service;

import io.boot.commons.mybatis.service.CrudService;
import io.boot.payment.dto.OrderDTO;
import io.boot.payment.entity.OrderEntity;

/**
 * 订单
 *
 * @author epiphyllum.zhou@gmail.com
 */
public interface OrderService extends CrudService<OrderEntity, OrderDTO> {


    OrderEntity getByOrderId(Long orderId);

    /**
     * 支付成功
     * @param order 订单
     */
    void paySuccess(OrderEntity order);
}