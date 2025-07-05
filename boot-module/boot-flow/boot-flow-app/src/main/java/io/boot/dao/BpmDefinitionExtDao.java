package io.boot.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.entity.BpmDefinitionExtEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程定义扩展
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Mapper
public interface BpmDefinitionExtDao extends BaseDao<BpmDefinitionExtEntity> {

}