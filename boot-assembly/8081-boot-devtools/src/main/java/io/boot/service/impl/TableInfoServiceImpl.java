/**
 * Copyright (c) 2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.boot.commons.mybatis.service.impl.BaseServiceImpl;
import io.boot.commons.tools.constant.Constant;
import io.boot.commons.tools.page.PageData;
import io.boot.dao.TableInfoDao;
import io.boot.entity.TableInfoEntity;
import io.boot.service.TableFieldService;
import io.boot.service.TableInfoService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;


/**
 * 表
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Service
public class TableInfoServiceImpl extends BaseServiceImpl<TableInfoDao, TableInfoEntity> implements TableInfoService {
    @Resource
    private TableFieldService tableFieldService;

    @Override
    public PageData<TableInfoEntity> page(Map<String, Object> params) {
        IPage<TableInfoEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );
        return new PageData<>(page.getRecords(), page.getTotal());
    }

    private QueryWrapper<TableInfoEntity> getWrapper(Map<String, Object> params) {
        String tableName = (String) params.get("tableName");

        QueryWrapper<TableInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(tableName), "table_name", tableName);

        return wrapper;
    }

    @Override
    public TableInfoEntity getByTableName(String tableName) {
        return baseDao.getByTableName(tableName);
    }

    @Override
    public void deleteByTableName(String tableName) {
        baseDao.deleteByTableName(tableName);
    }

    @Override
    public void deleteBatchIds(Long[] ids) {
        //删除表
        super.deleteBatchIds(Arrays.asList(ids));

        //删除列
        tableFieldService.deleteBatchTableIds(ids);
    }
}