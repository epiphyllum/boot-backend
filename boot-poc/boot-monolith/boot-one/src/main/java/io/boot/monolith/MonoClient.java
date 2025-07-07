package io.boot.monolith;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.boot.commons.tools.utils.Result;
import io.boot.monolith.dto.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MonoClient {

    @Resource
    private ObjectMapper objectMapper;

    // 请求
    private <T> String post(String body, BaseRequest<T> request) {
        if (request.getClass().equals(DemoRequest.class)) {

            return """
                    {
                    "code":0,
                    "msg":"success",
                    "data": {
                        "name": "hary",
                        "age": 110
                     }
                    }
                    """;

        }
        if (request.getClass().equals(PayRequest.class)) {
            return """
                    {
                    "code":0,
                    "msg":"success",
                    "data": {
                        "status": "hary",
                        "message": "success",
                        "orderCount": 110
                     }
                    }
                    """;
        }

        return null;
    }

    public <T> Result<T> execute(BaseRequest<T> request) throws JsonProcessingException {
        log.info("request: {}", request);
        String reqBody = objectMapper.writeValueAsString(request);
        String resBody = this.post(reqBody, request);
        log.info("request: {}, resBody: {}", request, resBody);
        Result<T> tResult = objectMapper.readValue(resBody, request.resultClass());
        return tResult;
    }

    // 接口1
    public Result<DemoResponse> demoInfo(DemoRequest request) throws JsonProcessingException {
        Result<DemoResponse> result = this.execute(request);
        log.info("result: {}", result);
        return result;
    }

    // 接口2
    public Result<PayResponse> pay(PayRequest request) throws JsonProcessingException {
        Result<PayResponse> result = this.execute(request);
        return result;
    }
}
