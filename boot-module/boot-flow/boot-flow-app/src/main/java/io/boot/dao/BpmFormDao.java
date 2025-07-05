package io.boot.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.entity.BpmFormEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工作流表单
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Mapper
public interface BpmFormDao extends BaseDao<BpmFormEntity> {

}