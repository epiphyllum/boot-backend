package io.boot.rbac.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.rbac.entity.ProductEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 产品管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface ProductDao extends BaseDao<ProductEntity> {
	
}