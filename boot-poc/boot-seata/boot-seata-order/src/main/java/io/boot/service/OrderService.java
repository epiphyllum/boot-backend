/**
 * Copyright (c) 2019 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
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
 * @author epiphyllum.zhou@gmail.com
 */
public interface OrderService extends CrudService<OrderEntity, OrderDTO> {

    /**
     * 创建订单、减库存，涉及到两个服务
     * @param commodityCode   商品编码
     * @param count           数量
     */
    void createOrder(String commodityCode, Integer count);
}