/**
 * Copyright (c) 2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.devtool.service;

import io.boot.devtool.config.DataSourceInfo;
import io.boot.devtool.entity.MenuEntity;
import io.boot.devtool.entity.TableFieldEntity;
import io.boot.devtool.entity.TableInfoEntity;

import java.util.List;

/**
 * 代码生成
 *
 * @author epiphyllum.zhou@gmail.com
 */
public interface GeneratorService {

    DataSourceInfo getDataSourceInfo(Long datasourceId);

    void datasourceTable(TableInfoEntity tableInfo);

    void updateTableField(Long tableId, List<TableFieldEntity> tableFieldList);

    void generatorCode(TableInfoEntity tableInfo);

    void generatorMenu(MenuEntity menu);
}
