package io.boot.service;

import io.boot.dto.AiChatMessageDTO;
import io.boot.dto.AiChatRequestDTO;
import reactor.core.publisher.Flux;

public interface AiChatService {

    Flux<AiChatMessageDTO> sendChatMessage(AiChatRequestDTO requestDTO);

}
