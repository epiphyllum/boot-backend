package io.boot.api.service;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.boot.api.dto.NotifyResponse;
import io.boot.api.utils.ApiContext;
import io.boot.commons.tools.exception.BootException;
import io.boot.commons.tools.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Service
public class NotifyService {
    @Resource
    private ObjectMapper objectMapper = new ObjectMapper();
    @Resource
    private RestTemplate restTemplate = new RestTemplate();

    // 类型推断
    private static ParameterizedTypeReference<Result<NotifyResponse>> notifyResponseType = new ParameterizedTypeReference<Result<NotifyResponse>>() {
    };

    // 通知客户
    public Result<NotifyResponse> notify(Object notify, String url, ApiContext context) {

        String platformPrivateKey = ""; // 平台私有钥: todo from context

        // 签名器
        RSA rsaSigner = new RSA(platformPrivateKey, null);
        Sign signer = SecureUtil.sign(SignAlgorithm.SHA512withRSA);
        signer.setPrivateKey(rsaSigner.getPrivateKey());

        // 请求体
        String reqBody = null;
        try {
            reqBody = objectMapper.writeValueAsString(notify);
        } catch (JsonProcessingException e) {
            // todo
            throw new BootException("通知异常");
        }

        // 验证签名
        String toSign = reqBody + context.getClientId() + context.getApiName() + context.getRequestId();
        byte[] bytes = DigestUtil.sha256(toSign);
        String sign = signer.signHex(bytes);

        // 发起通知
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-sign", sign);
        headers.add("x-client-id", context.getClientId());
        headers.add("x-req-id", context.getRequestId());
        headers.add("x-api", context.getApiName());
        RequestEntity reqEntity = RequestEntity.post("")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL)
                .acceptCharset(StandardCharsets.UTF_8)
                .headers(headers)
                .body(reqBody);

        ResponseEntity<Result<NotifyResponse>> responseEntity = restTemplate.exchange(url, HttpMethod.POST, reqEntity, notifyResponseType);
        return responseEntity.getBody();
    }
}
