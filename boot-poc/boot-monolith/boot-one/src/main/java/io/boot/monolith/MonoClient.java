package io.boot.monolith;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.boot.commons.tools.utils.Result;
import io.boot.monolith.dto.BaseRequest;
import io.boot.monolith.dto.DemoResponse;
import io.boot.monolith.dto.DemoRequest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MonoClient {

    @Resource
    private ObjectMapper objectMapper;

    // 请求
    private String post(String body) {
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

    public <T> Result<T> execute(BaseRequest<T> request) throws JsonProcessingException {
        log.info("request: {}", request);
        String reqBody = objectMapper.writeValueAsString(request);
        String resBody = this.post(reqBody);
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

}
