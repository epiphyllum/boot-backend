package io.boot.mp.dao;


import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.mp.entity.MpMenuEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* 公众号自定义菜单
*
* @author epiphyllum.zhou@gmail.com
*/
@Mapper
public interface MpMenuDao extends BaseDao<MpMenuEntity> {
	
}