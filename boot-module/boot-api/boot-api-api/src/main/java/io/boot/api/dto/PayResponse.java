package io.boot.api.dto;

import lombok.Data;

@Data
public class PayResponse {
    private Integer status;
    private String orderId;
}
