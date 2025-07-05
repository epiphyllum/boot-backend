/**
 * Copyright (c) 2019 人人开源 All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */
package io.boot.service;

import io.boot.commons.mybatis.service.CrudService;
import io.boot.dto.StorageDTO;
import io.boot.entity.StorageEntity;

/**
 * 库存表
 *
 * @author epiphyllum.zhou@gmail.com
 */
public interface StorageService extends CrudService<StorageEntity, StorageDTO> {

    /**
     * 减库存
     *
     * @param commodityCode 商品代码
     * @param count         数量
     */
    void deduct(String commodityCode, int count);

}