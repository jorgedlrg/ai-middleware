package com.jorgedelarosa.aimiddleware.adapter.out.web.dto;

import java.util.List;

/**
 * @author jorge
 */
public record GenericChatRequest(String model, List<GenericChatMessage> messages, Integer maxTokens) {}
