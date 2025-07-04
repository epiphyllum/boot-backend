/**
 * Copyright (c) 2020 人人开源 All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.service;


import io.boot.commons.mybatis.service.BaseService;
import io.boot.commons.tools.page.PageData;
import io.boot.entity.TableInfoEntity;

import java.util.Map;

/**
 * 表
 *
 * @author epiphyllum.zhou@gmail.com
 */
public interface TableInfoService extends BaseService<TableInfoEntity> {

    PageData<TableInfoEntity> page(Map<String, Object> params);

    TableInfoEntity getByTableName(String tableName);

    void deleteByTableName(String tableName);

    void deleteBatchIds(Long[] ids);
}