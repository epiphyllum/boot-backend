package io.boot.api.dto;

import io.boot.api.utils.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PayQueryRequest extends BaseRequest<PayQueryResponse> {
    @NotNull
    private String orderId;
}
