package io.boot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssssssss.magicapi.core.service.MagicAPIService;

import java.util.HashMap;
import java.util.Map;

/**
 * 演示如何调用magic-api里的接口
 */
@RestController
@RequestMapping("call")
@Slf4j
public class MagicTestController {

    @Resource
    private MagicAPIService magicAPIService;

    @Resource
    private ObjectMapper objectMapper;

    @GetMapping("/db")
    public String db() throws JsonProcessingException {
        Map<String, Object> params = new HashMap<>();
        // execute方式没有包装code + msg,  不走过滤器
        Object get = magicAPIService.execute("GET", "/test/db", params);
        // call方式有包装code + msg, 不走过滤器
        Object call = magicAPIService.call("GET", "/test/db", params);
        return objectMapper.writeValueAsString(get) + "\n" + objectMapper.writeValueAsString(call);
    }


}
