package io.boot.ai.dao;


import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.ai.entity.AiChatMessageEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * AI聊天消息
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface AiChatMessageDao extends BaseDao<AiChatMessageEntity> {

}