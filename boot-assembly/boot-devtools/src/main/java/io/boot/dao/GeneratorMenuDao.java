/**
 * Copyright (c) 2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 创建菜单
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Mapper
public interface GeneratorMenuDao {

    void generatorMenu(Map<String, Object> params);

    void generatorMenuLanguage(Map<String, Object> params);
}