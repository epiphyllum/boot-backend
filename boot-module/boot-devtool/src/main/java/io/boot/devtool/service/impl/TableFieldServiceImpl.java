/**
 * Copyright (c) 2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.devtool.service.impl;

import io.boot.commons.mybatis.service.impl.BaseServiceImpl;
import io.boot.devtool.dao.TableFieldDao;
import io.boot.devtool.service.TableFieldService;
import io.boot.devtool.entity.TableFieldEntity;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 表
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Service
public class TableFieldServiceImpl extends BaseServiceImpl<TableFieldDao, TableFieldEntity> implements TableFieldService {

    @Override
    public List<TableFieldEntity> getByTableName(String tableName) {
        return baseDao.getByTableName(tableName);
    }

    @Override
    public void deleteByTableName(String tableName) {
        baseDao.deleteByTableName(tableName);
    }

    @Override
    public void deleteBatchTableIds(Long[] tableIds) {
        baseDao.deleteBatchTableIds(tableIds);
    }

}