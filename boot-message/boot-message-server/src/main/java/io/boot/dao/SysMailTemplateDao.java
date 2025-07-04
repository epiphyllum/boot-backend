/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.entity.SysMailTemplateEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 邮件模板
 * 
 * @author epiphyllum.zhou@gmail.com
 */
@Mapper
public interface SysMailTemplateDao extends BaseDao<SysMailTemplateEntity> {
	
}
