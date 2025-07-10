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

@RestController
@RequestMapping("call")
@Slf4j
public class CallMagicController {

    @Resource
    private MagicAPIService magicAPIService;

    @Resource
    private ObjectMapper objectMapper;

    // 调用magic script: 没有包含 code+msg的包装
    @GetMapping("/db")
    public String db() throws JsonProcessingException {
        Map<String, Object> params = new HashMap<>();
        Object get = magicAPIService.execute("GET", "/test/db", params);
        Object call = magicAPIService.call("GET", "/test/db", params);
        return objectMapper.writeValueAsString(get) + "\n" + objectMapper.writeValueAsString(call);
    }


}
