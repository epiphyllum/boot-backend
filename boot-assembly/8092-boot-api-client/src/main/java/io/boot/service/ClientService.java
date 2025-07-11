package io.boot.config;

import io.boot.api.dto.PayQueryRequest;
import io.boot.api.dto.PayQueryResponse;
import io.boot.api.dto.PayRequest;
import io.boot.api.dto.PayResponse;
import io.boot.utils.ApiRequester;
import io.boot.api.utils.ApiResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ApiClient {
    public static String API_PAY = "pay";
    public static String API_PAY_QUERY = "payQuery";

    @Resource
    private ApiRequester apiRequester;

    public ApiResult<PayResponse> pay(PayRequest payRequest) {
        return apiRequester.execute(payRequest, API_PAY, "requestId");
    }
    public ApiResult<PayQueryResponse> payQuery(PayQueryRequest payQueryRequest) {
        return apiRequester.execute(payQueryRequest, API_PAY_QUERY, "requestId");
    }
}
