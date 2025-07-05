package io.boot.ai.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.ai.entity.AiChatConversationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * AI聊天对话
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface AiChatConversationDao extends BaseDao<AiChatConversationEntity> {

}