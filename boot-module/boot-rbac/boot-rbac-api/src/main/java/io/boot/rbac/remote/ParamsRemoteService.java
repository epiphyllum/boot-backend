/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.boot.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.rbac.remote;

import io.boot.commons.tools.exception.ErrorCode;
import io.boot.commons.tools.exception.BootException;
import io.boot.commons.tools.utils.JsonUtils;
import io.boot.rbac.feign.ParamsFeignClient;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
 
/**
 * 参数
 *
 * @author Mark sunlightcs@gmail.com
 */
@Component
public class ParamsRemoteService {
    @Resource
    private ParamsFeignClient paramsFeignClient;

    /**
     * 根据参数编码，获取value的Object对象
     *
     * @param paramCode 参数编码
     * @param clazz     Object对象
     */
    public <T> T getValueObject(String paramCode, Class<T> clazz) {
        String paramValue = paramsFeignClient.getValue(paramCode);
        if (StringUtils.isNotBlank(paramValue)) {
            return JsonUtils.parseObject(paramValue, clazz);
        }

        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new BootException(ErrorCode.PARAMS_GET_ERROR);
        }
    }

    /**
     * 根据参数编码，更新value
     *
     * @param paramCode  参数编码
     * @param paramValue 参数值
     */
    public void updateValueByCode(String paramCode, String paramValue) {
        paramsFeignClient.updateValueByCode(paramCode, paramValue);
    }

}