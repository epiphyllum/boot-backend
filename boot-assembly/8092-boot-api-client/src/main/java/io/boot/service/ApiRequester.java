package io.boot.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.boot.api.utils.ApiResult;
import io.boot.api.utils.BaseRequest;
import io.boot.commons.tools.exception.BootException;
import io.boot.config.ClientConfig;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.initialization.qual.Initialized;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
public class ApiRequester {

    @Resource
    private ClientConfig clientConfig;

    // 需要初始化字段
    private Sign signer;
    private Sign verifier;
    private ObjectMapper objectMapper;
    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        this.objectMapper = new ObjectMapper();
        this.restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = this.restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> messageConverter : messageConverters) {
            if (messageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) messageConverter).setDefaultCharset(Charset.forName("UTF8"));
            }
        }

        // 签名器
        RSA rsaSigner = new RSA(this.clientConfig.getPrivateKey(), null);
        this.signer = SecureUtil.sign(SignAlgorithm.SHA512withRSA);
        this.signer.setPrivateKey(rsaSigner.getPrivateKey());
        // 验证器
        RSA rsaVerifier = new RSA(null, this.clientConfig.getPlatformKey());
        this.verifier = SecureUtil.sign(SignAlgorithm.SHA512withRSA);
        this.verifier.setPublicKey(rsaVerifier.getPublicKey());
    }

    // todo: add code and exception
    public <T> ApiResult<T> execute(BaseRequest<T> request, String apiName, String reqId) {
        log.debug("request: {}", request);
        String reqBody = null;
        try {
            reqBody = this.objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new BootException("invalid request, can not convert to json");
        }
        String sign = this.signer.signHex(reqBody);

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-sign", sign);
        headers.add("x-client-id", this.clientConfig.getClientId());
        headers.add("x-req-id", reqId);
        headers.add("x-api", apiName);
        RequestEntity reqEntity = RequestEntity.post("")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL)
                .acceptCharset(StandardCharsets.UTF_8)
                .headers(headers)
                .body(reqBody);

        ParameterizedTypeReference<ApiResult<T>> typeReference = request.resultClass();
        ResponseEntity<ApiResult<T>> exchange = restTemplate.exchange(this.clientConfig.getApiUrl(), HttpMethod.POST, reqEntity, typeReference);
        ApiResult<T> body = exchange.getBody();
        log.debug("response: {}", body);
        return body;

    }
}
