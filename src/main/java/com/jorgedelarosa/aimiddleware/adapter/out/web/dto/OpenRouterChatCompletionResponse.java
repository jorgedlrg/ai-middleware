package com.jorgedelarosa.aimiddleware.adapter.out.web.dto;

import java.util.List;

/**
 * @author jorge
 */
public record OpenRouterChatCompletionResponse(
    String id, List<OpenRouterChatCompletionResponseChoice> choices) {

  public record OpenRouterChatCompletionResponseChoice(OpenRouterChatCompletionMessage message) {}
}
