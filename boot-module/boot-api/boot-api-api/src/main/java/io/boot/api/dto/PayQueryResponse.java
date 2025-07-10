package io.boot.api.dto;

import lombok.Data;

@Data
public class PayQueryResponse {
    private Integer status;
    private String orderId;
}
