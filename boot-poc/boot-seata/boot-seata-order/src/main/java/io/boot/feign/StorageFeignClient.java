/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.feign;

import io.boot.commons.tools.utils.Result;
import io.boot.feign.fallback.StorageFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 库存 Feign Client
 *
 * @author epiphyllum.zhou@gmail.com
 */
@FeignClient(name = "boot-seata-storage", fallbackFactory = StorageFeignClientFallbackFactory.class)
public interface StorageFeignClient {

	/**
	 * 减库存
	 *
	 * @param commodityCode 商品代码
	 * @param count         数量
	 */
	@PutMapping("seata-storage/storage/deduct")
	Result deduct(@RequestParam("commodityCode") String commodityCode, @RequestParam("count") Integer count);

}
