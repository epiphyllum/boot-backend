package io.boot.api.service;

import io.boot.api.dto.OrderRequest;
import io.boot.api.dto.OrderResponse;
import io.boot.api.utils.ApiBase;
import io.boot.api.utils.ApiContext;
import io.boot.commons.tools.utils.Result;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("order")
public class OrderService implements ApiBase {
    Result<OrderResponse> createOrder(OrderRequest orderRequest, ApiContext apiContext) {
        Result<OrderResponse> result = new Result<>();
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(orderResponse.getOrderId());
        orderResponse.setTotalPrice(new BigDecimal("100"));
        result.setData(orderResponse);
        return result;
    }
}
