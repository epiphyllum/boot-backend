/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.boot.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.rbac.redis;

import io.boot.commons.tools.redis.RedisKeys;
import io.boot.commons.tools.redis.RedisUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 参数管理
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Component
public class SysParamsRedis {
    @Resource
    private RedisUtils redisUtils;

    public void delete(Object[] paramCodes) {
        String key = RedisKeys.getSysParamsKey();
        redisUtils.hDel(key, paramCodes);
    }

    public void set(String paramCode, String paramValue) {
        if (paramValue == null) {
            return;
        }
        String key = RedisKeys.getSysParamsKey();
        redisUtils.hSet(key, paramCode, paramValue);
    }

    public String get(String paramCode) {
        String key = RedisKeys.getSysParamsKey();
        return (String) redisUtils.hGet(key, paramCode);
    }

}
