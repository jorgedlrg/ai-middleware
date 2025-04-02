package com.jorgedelarosa.aimiddleware.adapter.out.web.dto;

import java.util.List;

/**
 * @author jorge
 */
public record OpenRouterChatCompletionRequest(
    String model, List<OpenRouterChatCompletionMessage> messages) {}
