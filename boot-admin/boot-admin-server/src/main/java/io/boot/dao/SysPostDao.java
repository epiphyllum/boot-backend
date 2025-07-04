/**
 * Copyright (c) 2021 人人开源 All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.entity.SysPostEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* 岗位管理
*
* @author epiphyllum.zhou@gmail.com
*/
@Mapper
public interface SysPostDao extends BaseDao<SysPostEntity> {
	
}