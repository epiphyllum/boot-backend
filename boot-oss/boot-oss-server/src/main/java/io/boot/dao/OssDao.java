/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.entity.OssEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件上传
 * 
 * @author epiphyllum.zhou@gmail.com
 */
@Mapper
public interface OssDao extends BaseDao<OssEntity> {
	
}
