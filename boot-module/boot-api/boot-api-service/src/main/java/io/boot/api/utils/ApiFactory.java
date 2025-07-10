package io.boot.api.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.boot.commons.tools.exception.BootException;
import io.boot.commons.tools.utils.ByteUtil;
import io.boot.commons.tools.utils.Result;
import io.boot.commons.tools.validator.ValidatorUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ApiFactory {

    @Resource
    private List<ApiBase> instances;
    @Resource
    private ObjectMapper objectMapper;

    private void initService(Object object, Map<String, ApiMeta> metaMap) {
        Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            // 获取返回类型
            Class<?> returnType = method.getReturnType();
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (returnType == Result.class &&
                    parameterTypes.length == 2 &&
                    parameterTypes[1].equals(ApiContext.class)
            ) {
                metaMap.put(method.getName(), new ApiMeta(object, method, parameterTypes[0]));
            }
        }
    }

    // API元数据
    public static Map<String, ApiMeta> metaMap = new HashMap<>();

    private void getClient() {
        return;
    }

    @PostConstruct
    public void init() {
        for (ApiBase instance : instances) {
            initService(instance, metaMap);
        }
    }

    // 调用
    public Result<?> call(ApiContext context) {
        // 检查白名单: client对象有白名单
        context.checkIp();

        // 验证签名
        context.verify();

        // 获取api处理元数据
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
