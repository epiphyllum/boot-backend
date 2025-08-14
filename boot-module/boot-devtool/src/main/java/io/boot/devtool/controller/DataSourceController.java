/**
 * Copyright (c) 2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.devtool.controller;

import io.boot.commons.tools.page.PageData;
import io.boot.commons.tools.utils.Result;
import io.boot.devtool.config.DataSourceInfo;
import io.boot.devtool.entity.DataSourceEntity;
import io.boot.devtool.entity.TableInfoEntity;
import io.boot.devtool.service.DataSourceService;
import io.boot.devtool.service.GeneratorService;
import io.boot.devtool.utils.DbUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 数据源管理
 *
 * @author epiphyllum.zhou@gmail.com
 */
@RestController
@RequestMapping("devtool/datasource")
public class DataSourceController {
    @Resource
    private DataSourceService datasourceService;

    @Resource
    private GeneratorService generatorService;

    @GetMapping("page")
    public Result<PageData<DataSourceEntity>> page(@RequestParam Map<String, Object> params) {
        PageData<DataSourceEntity> page = datasourceService.page(params);

        return new Result<PageData<DataSourceEntity>>().ok(page);
    }

    @GetMapping("list")
    public Result<List<DataSourceEntity>> list() {
        List<DataSourceEntity> list = datasourceService.list();

        return new Result<List<DataSourceEntity>>().ok(list);
    }

    @GetMapping("{id}")
    public Result<DataSourceEntity> get(@PathVariable("id") Long id) {
        DataSourceEntity data = datasourceService.selectById(id);

        return new Result<DataSourceEntity>().ok(data);
    }

    @GetMapping("test/{id}")
    public Result<String> test(@PathVariable("id") Long id) {
        try {
            DataSourceEntity entity = datasourceService.selectById(id);

            DbUtils.getConnection(new DataSourceInfo(entity));
            return new Result<String>().ok("连接成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<String>().error("连接失败，请检查配置信息");
        }
    }

    @PostMapping
    public Result save(@RequestBody DataSourceEntity entity) {
        datasourceService.insert(entity);

        return new Result();
    }

    @PutMapping
    public Result update(@RequestBody DataSourceEntity entity) {
        datasourceService.updateById(entity);

        return new Result();
    }

    @DeleteMapping
    public Result delete(@RequestBody Long[] ids) {
        datasourceService.deleteBatchIds(Arrays.asList(ids));

        return new Result();
    }

    /**
     * 获取数据源中所有表
     */
    @GetMapping("table/list/{id}")
    public Result<List<TableInfoEntity>> getDataSourceTableList(@PathVariable("id") Long id) {
        try {
            //初始化配置信息
            DataSourceInfo info = generatorService.getDataSourceInfo(id);
            List<TableInfoEntity> tableInfoList = DbUtils.getTablesInfoList(info);
            return new Result<List<TableInfoEntity>>().ok(tableInfoList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<List<TableInfoEntity>>().error("数据源配置错误，请检查数据源配置！");
        }
    }
    /**
     * 导入数据源中的表
     */
    @PostMapping("table")
    public Result datasourceTable(@RequestBody TableInfoEntity tableInfo) {
        generatorService.datasourceTable(tableInfo);
        return new Result();
    }

}