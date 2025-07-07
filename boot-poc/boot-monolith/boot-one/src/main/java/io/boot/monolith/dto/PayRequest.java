package io.boot.monolith.dto;

import lombok.Data;

@Data
public class PayRequest extends BaseRequest<PayResponse> {
    private Integer orderId;
}
