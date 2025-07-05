package io.boot.ai.service;

import io.boot.ai.dto.AiChatMessageDTO;
import io.boot.ai.dto.AiChatRequestDTO;
import reactor.core.publisher.Flux;

public interface AiChatService {

    Flux<AiChatMessageDTO> sendChatMessage(AiChatRequestDTO requestDTO);

}
