/**
 * Copyright (c) 2019 人人开源 All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.boot.commons.mybatis.service.impl.CrudServiceImpl;
import io.boot.commons.tools.exception.RenException;
import io.boot.dao.StorageDao;
import io.boot.dto.StorageDTO;
import io.boot.entity.StorageEntity;
import io.boot.service.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 库存表
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
public class StorageServiceImpl extends CrudServiceImpl<StorageDao, StorageEntity, StorageDTO> implements StorageService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deduct(String commodityCode, int count) {
        int updateCount = baseDao.updateDeduct(commodityCode, count);
        if (updateCount == 0) {
            throw new RenException("库存数不足，请稍后再试！");
        }
    }

    @Override
    public QueryWrapper<StorageEntity> getWrapper(Map<String, Object> params) {
        return null;
    }

}