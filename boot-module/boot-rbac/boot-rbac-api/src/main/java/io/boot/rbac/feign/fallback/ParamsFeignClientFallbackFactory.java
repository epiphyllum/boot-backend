/**
 * Copyright (c) 2016-2020 人人开源 All rights reserved.
 * <p>
 * https://www.boot.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.rbac.feign.fallback;

import io.boot.rbac.feign.ParamsFeignClient;
import org.springframework.stereotype.Component;

/**
 * 参数接口 FallbackFactory
 *
 * @author Mark sunlightcs@gmail.com
 */
@Component
public class ParamsFeignClientFallbackFactory implements ParamsFeignClient {
    @Override
    public String getValue(String paramCode) {
        return null;
    }

    @Override
    public void updateValueByCode(String paramCode, String paramValue) {

    }
}
