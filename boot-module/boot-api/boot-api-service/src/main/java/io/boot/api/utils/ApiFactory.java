package io.boot.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.boot.commons.tools.exception.BootException;
import io.boot.commons.tools.utils.Result;
import io.boot.commons.tools.validator.ValidatorUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ApiFactory {

    @Resource
    private Map<String, ApiBase> serviceMap;

    @Resource
    private ObjectMapper objectMapper;

    public static Map<String, ApiMeta> metaMap = new HashMap<>();


    @PostConstruct
    public void init() {
        serviceMap.forEach((k, v) -> {
            initService(k, v, metaMap);

        });
    }

    private void initService(String serviceName, Object instance, Map<String, ApiMeta> metaMap) {
        Method[] methods = instance.getClass().getMethods();
        for (Method method : methods) {
            // 获取返回类型
            Class<?> returnType = method.getReturnType();
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (returnType == Result.class &&
                    parameterTypes.length == 2 &&
                    parameterTypes[1].equals(ApiContext.class)
            ) {
                String fullKey = serviceName + "." + method.getName();
                metaMap.put(fullKey, new ApiMeta(instance, method, parameterTypes[0]));
            }
        }
    }

    // 调用
    public Result<?> call(ApiContext context) {
        // 检查白名单: client对象有白名单
        context.checkIp();

        // 验证签名
        context.verify();

        // 服务元数据
        ApiMeta apiMeta = metaMap.get(context.getApiName());

        // 解析请求
        Object req = null;
        try {
            req = objectMapper.readValue(context.getBody(), apiMeta.getReqClass());
        } catch (JsonProcessingException e) {
            throw new BootException("invalid request");
        }

        // 参数验证
        ValidatorUtils.validateEntity(req); // 参数检查

        // 调用服务
        Result<?> result = null;
        try {
            result = (Result) apiMeta.getMethod().invoke(apiMeta.getInstance(), req, context);
        } catch (IllegalAccessException e) {
            throw new BootException("invalid");
        } catch (InvocationTargetException e) {
            throw new BootException("invalid");
        }

        // 结束
        return result;
    }
}
