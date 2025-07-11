/**
 * Copyright (c) 2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.service;

import io.boot.config.DataSourceInfo;
import io.boot.entity.MenuEntity;
import io.boot.entity.TableFieldEntity;
import io.boot.entity.TableInfoEntity;

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
