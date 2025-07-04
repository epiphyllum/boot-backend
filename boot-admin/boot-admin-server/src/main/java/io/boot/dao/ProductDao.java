package io.boot.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.entity.ProductEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 产品管理
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Mapper
public interface ProductDao extends BaseDao<ProductEntity> {
	
}