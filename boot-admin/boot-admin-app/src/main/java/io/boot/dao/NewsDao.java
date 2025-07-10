/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.boot.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.entity.NewsEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 新闻
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface NewsDao extends BaseDao<NewsEntity> {

    List<NewsEntity> getList(Map<String, Object> params);
	
}
