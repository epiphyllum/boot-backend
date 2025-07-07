/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.boot.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.service;

import io.boot.commons.dynamic.datasource.annotation.DataSource;
import io.boot.commons.mybatis.service.impl.BaseServiceImpl;
import io.boot.dao.SysUserDao;
import io.boot.entity.SysUserEntity;

/**
 * 测试多数据源
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.1.0
 */
@DataSource("slave2")
public class DynamicService extends BaseServiceImpl<SysUserDao, SysUserEntity> {
}
