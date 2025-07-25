/**
 * Copyright (c) 2019 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.boot.controller;

import io.boot.commons.tools.utils.Result;
import io.boot.service.StorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 库存表
 *
 * @author epiphyllum.zhou@gmail.com
 */
@RestController
@RequestMapping("storage")
@Tag(name = "库存表")
public class StorageController {
    @Resource
    private StorageService storageService;

    /**
     * 减库存
     *
     * @param commodityCode 商品编码
     * @param count         数量
     */
    @RequestMapping(path = "deduct")
    public Result deduct(String commodityCode, Integer count) {
        storageService.deduct(commodityCode, count);

        return new Result();
    }

}