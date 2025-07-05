/**
 * Copyright (c) 2019 人人开源 All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.boot.commons.mybatis.service.impl.CrudServiceImpl;
import io.boot.commons.tools.exception.RenException;
import io.boot.commons.tools.utils.Result;
import io.boot.dao.OrderDao;
import io.boot.dto.OrderDTO;
import io.boot.entity.OrderEntity;
import io.boot.feign.StorageFeignClient;
import io.boot.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 订单
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Service
public class OrderServiceImpl extends CrudServiceImpl<OrderDao, OrderEntity, OrderDTO> implements OrderService {
    @Resource
    private StorageFeignClient storageFeignClient;

    /**
     * 创建订单、减库存，涉及到两个服务
     *
     * @param commodityCode 商品编码
     * @param count         数量
     */
    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(String commodityCode, Integer count) {
        //商品金额
        BigDecimal commodityMoney = new BigDecimal(10);
        //订单金额
        BigDecimal orderMoney = new BigDecimal(count).multiply(commodityMoney);
        OrderEntity order = new OrderEntity()
                .setCommodityCode(commodityCode)
                .setCount(count)
                .setMoney(orderMoney);

        //减库存
        Result result = storageFeignClient.deduct(commodityCode, count);
        if (!result.success()) {
            throw new RenException(result.getMsg());
        }

        //模拟异常
        int i = 1 / 0;

        //保存订单
        baseDao.insert(order);
    }

    @Override
    public QueryWrapper<OrderEntity> getWrapper(Map<String, Object> params) {
        return null;
    }

}