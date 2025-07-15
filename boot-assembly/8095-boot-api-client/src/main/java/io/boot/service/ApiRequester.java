package io.boot.service;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.boot.api.utils.ApiResult;
import io.boot.api.utils.BaseRequest;
import io.boot.commons.tools.exception.BootException;
import io.boot.config.ClientConfig;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.TimeZone;

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
        objectMapper = new ObjectMapper();
        //忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // java8时间
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //日期格式转换
        //mapper.setDateFormat(new SimpleDateFormat(DateUtils.DATE_PATTERN));
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //Long类型转String类型
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);

        // 配置RestTemplate
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
        log.debug("request: {}|{}|{}", reqId, apiName, request);

        // 生成请求体字符串
        String reqBody = null;
        try {
            reqBody = this.objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new BootException("invalid request, can not convert to json");
        }

        // 对请求体计算签名
        String sign = this.signer.signHex(reqBody);

        // 设置头部信息
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-sign", sign);
        headers.add("x-client-id", this.clientConfig.getClientId());
        headers.add("x-req-id", reqId);
        headers.add("x-api", apiName);

        // 生成请求
        RequestEntity reqEntity = RequestEntity.post("")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL)
                .acceptCharset(StandardCharsets.UTF_8)
                .headers(headers)
                .body(reqBody);

        // 请求并拿到应答
        ParameterizedTypeReference<ApiResult<T>> typeReference = request.resultClass();
        ResponseEntity<ApiResult<T>> exchange = restTemplate.exchange(this.clientConfig.getApiUrl(), HttpMethod.POST, reqEntity, typeReference);
        ApiResult<T> body = exchange.getBody();
        log.debug("response: {}", body);
        return body;

    }
}
