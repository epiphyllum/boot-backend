package io.boot.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderResponse {
    private String orderId;
    private BigDecimal totalPrice;
}
