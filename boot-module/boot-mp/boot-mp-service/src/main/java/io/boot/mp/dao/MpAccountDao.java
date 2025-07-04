package io.boot.mp.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.mp.entity.MpAccountEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* 公众号账号管理
*
* @author epiphyllum.zhou@gmail.com
*/
@Mapper
public interface MpAccountDao extends BaseDao<MpAccountEntity> {
	
}