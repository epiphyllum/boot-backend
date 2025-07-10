/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.message.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.message.entity.SysMailLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 邮件发送记录
 * 
 * @author epiphyllum.zhou@gmail.com
 */
@Mapper
public interface SysMailLogDao extends BaseDao<SysMailLogEntity> {
	
}
