/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.boot.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.rbac.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.rbac.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色管理
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Mapper
public interface SysRoleDao extends BaseDao<SysRoleEntity> {
	
}
