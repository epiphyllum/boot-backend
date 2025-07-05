package io.boot.ai.service;


import io.boot.commons.mybatis.service.BaseService;
import io.boot.ai.dto.AiChatMessageDTO;
import io.boot.ai.entity.AiChatMessageEntity;

import java.util.List;

/**
 * AI聊天消息
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface AiChatMessageService extends BaseService<AiChatMessageEntity> {

    List<AiChatMessageDTO> getList(Long conversationId);

    void clearList(Long conversationId);

}