/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.boot.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.rbac.feign;

import io.boot.commons.tools.constant.ServiceConstant;
import io.boot.commons.tools.utils.Result;
import io.boot.rbac.dto.SysDictTypeDTO;
import io.boot.rbac.feign.fallback.DictFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 字典接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@FeignClient(name = ServiceConstant.RENREN_ADMIN_SERVER, contextId = "DictFeignClient", fallbackFactory = DictFeignClientFallbackFactory.class)
public interface DictFeignClient {

    /**
     * 字典类型列表
     */
    @GetMapping("sys/dict/type/list")
    Result<List<SysDictTypeDTO>> getDictTypeList();

}