package io.boot.api.service;

import io.boot.api.dto.PayQueryRequest;
import io.boot.api.dto.PayQueryResponse;
import io.boot.api.dto.PayRequest;
import io.boot.api.dto.PayResponse;
import io.boot.api.utils.ApiBase;
import io.boot.api.utils.ApiContext;
import io.boot.commons.tools.utils.Result;
import org.springframework.stereotype.Service;

@Service
public class PayService implements ApiBase {

    // pay就是API名称
    public Result<PayResponse> pay(PayRequest request, ApiContext apiContext) {
        return null;
    }

    // query就是API名称
    public Result<PayQueryResponse> query(PayQueryRequest request, ApiContext apiContext) {
        return null;
    }
}
