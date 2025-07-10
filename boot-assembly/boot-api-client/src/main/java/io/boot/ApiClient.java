package io.boot;

import io.boot.api.dto.PayQueryRequest;
import io.boot.api.dto.PayQueryResponse;
import io.boot.api.dto.PayRequest;
import io.boot.api.dto.PayResponse;
import io.boot.api.utils.ApiRequester;
import io.boot.api.utils.ApiResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ApiClient {
    @Resource
    private ApiRequester apiRequester;

    public ApiResult<PayResponse> pay(PayRequest payRequest) {
        return apiRequester.execute(payRequest, "pay", "requestId");
    }
    public ApiResult<PayQueryResponse> payQuery(PayQueryRequest payQueryRequest) {
        return apiRequester.execute(payQueryRequest, "payQuery", "requestId");
    }
}
