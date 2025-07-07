package io.boot.monolith.dto;

import lombok.Data;

@Data
public class PayResponse {
    private Integer status;
    private String message;
    private String orderCount;
}
