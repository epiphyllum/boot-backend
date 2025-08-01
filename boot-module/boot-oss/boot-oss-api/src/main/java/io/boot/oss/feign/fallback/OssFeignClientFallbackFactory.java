/**
 * Copyright (c) 2016-2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.oss.feign.fallback;

import io.boot.commons.tools.utils.Result;
import io.boot.oss.dto.UploadDTO;
import io.boot.oss.feign.OssFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * OSS FallbackFactory
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Slf4j
@Component
public class OssFeignClientFallbackFactory implements FallbackFactory<OssFeignClient> {
    @Override
    public OssFeignClient create(Throwable throwable) {
        log.error("{}", throwable);

        return file -> new Result<UploadDTO>().error();
    }
}
