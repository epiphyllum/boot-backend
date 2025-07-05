package io.boot.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.entity.BpmInstanceExtEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程实例扩展
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Mapper
public interface BpmInstanceExtDao extends BaseDao<BpmInstanceExtEntity> {

}