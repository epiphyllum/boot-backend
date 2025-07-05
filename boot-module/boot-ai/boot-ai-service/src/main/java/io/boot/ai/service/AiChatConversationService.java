package io.boot.ai.service;

import io.boot.commons.mybatis.service.BaseService;
import io.boot.ai.dto.AiChatConversationDTO;
import io.boot.ai.entity.AiChatConversationEntity;

import java.util.List;

/**
 * AI聊天对话
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface AiChatConversationService extends BaseService<AiChatConversationEntity> {

    List<AiChatConversationDTO> getList();

    AiChatConversationDTO save(AiChatConversationDTO dto);

    void update(AiChatConversationDTO dto);

    void delete(List<Long> idList);

}