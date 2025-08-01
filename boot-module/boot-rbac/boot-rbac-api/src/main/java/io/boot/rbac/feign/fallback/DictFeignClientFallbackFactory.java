/**
 * Copyright (c) 2016-2020 BootCloud All rights reserved.
 * <p>
 * https://www.boot.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.rbac.feign.fallback;

import io.boot.commons.tools.utils.Result;
import io.boot.rbac.feign.DictFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 字典接口 FallbackFactory
 *
 * @author Mark sunlightcs@gmail.com
 */
@Slf4j
@Component
public class DictFeignClientFallbackFactory implements FallbackFactory<DictFeignClient> {
    @Override
    public DictFeignClient create(Throwable throwable) {
        log.error("{}", throwable);
        return () -> new Result<>();
    }
}
