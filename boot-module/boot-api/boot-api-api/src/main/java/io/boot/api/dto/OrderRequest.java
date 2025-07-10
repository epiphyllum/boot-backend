package io.boot.api.dto;

import io.boot.api.utils.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequest extends BaseRequest<OrderResponse> {
    private Integer itemCount;
    private BigDecimal itemPrice;
    private String orderId;
}
