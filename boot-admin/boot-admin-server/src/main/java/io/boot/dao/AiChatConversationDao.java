package io.boot.dao;

import io.boot.commons.mybatis.dao.BaseDao;
import io.boot.entity.AiChatConversationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * AI聊天对话
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Mapper
public interface AiChatConversationDao extends BaseDao<AiChatConversationEntity> {

}