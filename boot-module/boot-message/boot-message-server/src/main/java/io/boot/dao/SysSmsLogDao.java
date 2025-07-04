package io.boot.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.entity.SysSmsLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短信日志
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Mapper
public interface SysSmsLogDao extends BaseDao<SysSmsLogEntity> {
	
}