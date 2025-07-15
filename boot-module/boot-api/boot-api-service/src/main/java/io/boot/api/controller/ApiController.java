package io.boot.api.controller;

import io.boot.api.utils.ApiContext;
import io.boot.api.utils.ApiFactory;
import io.boot.commons.tools.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public class ApiController {
    @Resource
    private ApiFactory apiFactory;

    @PostMapping
    public Result<?> handle(@RequestBody String body,
                            @RequestHeader("x-sign") String sign,
                            @RequestParam("x-req-id") String reqId,
                            @RequestParam("x-isv-id") String isvId,
                            @RequestParam("x-app-id") String appId,
                            @RequestParam("x-api") String apiName
                             ) {
        ApiContext apiContext = new ApiContext();
        apiContext.setApiName(apiName);
        apiContext.setAppId(appId);
        apiContext.setSign(sign);
        apiContext.setRequestId(reqId);
        apiContext.setBody(body);
        apiContext.setClientEntity(null); // todo 客户端对象, 查询
        return apiFactory.call(apiContext);
    }
}
