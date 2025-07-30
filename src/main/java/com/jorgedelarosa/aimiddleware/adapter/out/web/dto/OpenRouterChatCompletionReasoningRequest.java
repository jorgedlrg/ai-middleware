package com.jorgedelarosa.aimiddleware.adapter.out.web.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

/**
 * @author jorge
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OpenRouterChatCompletionReasoningRequest(
    String model, List<OpenRouterChatCompletionMessage> messages, OpenRouterReasoning reasoning) {}
