/**
 * Copyright (c) 2019 人人开源 All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.boot.controller;

import io.boot.commons.tools.utils.Result;
import io.boot.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 订单
 *
 * @author epiphyllum.zhou@gmail.com
 */
@RestController
@RequestMapping("order")
@Tag(name = "订单")
public class OrderController {
    @Resource
    private OrderService orderService;

    @PostMapping("create")
    @Operation(summary = "创建订单")
    public Result createOrder(String commodityCode, Integer count) {
        //创建订单、减库存，涉及到两个服务
        orderService.createOrder(commodityCode, count);

        return new Result();
    }


}