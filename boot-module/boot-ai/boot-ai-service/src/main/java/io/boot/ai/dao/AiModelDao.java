package io.boot.ai.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.ai.entity.AiModelEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * AI模型
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface AiModelDao extends BaseDao<AiModelEntity> {

}