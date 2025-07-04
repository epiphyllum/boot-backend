package io.boot.service;

import io.boot.commons.mybatis.service.BaseService;
import io.boot.dto.AiChatConversationDTO;
import io.boot.entity.AiChatConversationEntity;

import java.util.List;

/**
 * AI聊天对话
 *
 * @author epiphyllum.zhou@gmail.com
 */
public interface AiChatConversationService extends BaseService<AiChatConversationEntity> {

    List<AiChatConversationDTO> getList();

    AiChatConversationDTO save(AiChatConversationDTO dto);

    void update(AiChatConversationDTO dto);

    void delete(List<Long> idList);

}