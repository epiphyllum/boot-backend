/**
 * Copyright (c) 2016-2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.feign.fallback;

import io.boot.commons.tools.utils.Result;
import io.boot.feign.StorageFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 库存 Feign Client FallbackFactory
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Slf4j
@Component
public class StorageFeignClientFallbackFactory implements FallbackFactory<StorageFeignClient> {
    @Override
    public StorageFeignClient create(Throwable throwable) {
        log.error("{}", throwable);

        return (commodityCode, count) -> new Result().error();
    }
}
