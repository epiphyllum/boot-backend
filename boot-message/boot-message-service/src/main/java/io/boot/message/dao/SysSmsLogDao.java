package io.boot.message.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.message.entity.SysSmsLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短信日志
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Mapper
public interface SysSmsLogDao extends BaseDao<SysSmsLogEntity> {
	
}