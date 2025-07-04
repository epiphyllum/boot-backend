package io.boot.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.entity.ExcelDataEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* Excel导入演示
*
* @author epiphyllum.zhou@gmail.com
*/
@Mapper
public interface ExcelDataDao extends BaseDao<ExcelDataEntity> {
	
}