package io.boot.api.controller;

import io.boot.api.utils.ApiContext;
import io.boot.api.utils.ApiFactory;
import io.boot.commons.tools.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public class ApiController {
    @Resource
    private ApiFactory apiFactory;

    @PostMapping
    public Result<?> handle(@RequestBody String body,
                            @RequestHeader("x-client-id") String clientId,
                            @RequestHeader("x-sign") String sign,
                            @RequestHeader("x-req-id") String reqId,
                            @RequestHeader("x-api") String apiName
                             ) {
        ApiContext apiContext = new ApiContext();
        apiContext.setApiName(apiName);
        apiContext.setClientId(apiName);
        apiContext.setSign(sign);
        apiContext.setRequestId(reqId);
        apiContext.setBody(body);
        apiContext.setClientEntity(null); // todo 客户端对象, 查询
        return apiFactory.call(apiContext);
    }
}
