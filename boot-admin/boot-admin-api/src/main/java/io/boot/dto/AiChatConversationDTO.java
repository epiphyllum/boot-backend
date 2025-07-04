package io.boot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * AI聊天对话
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Data
@Schema(description = "AI聊天对话")
public class AiChatConversationDTO {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "对话标题")
    private String title;

    @Schema(description = "模型ID")
    private Long modelId;

}