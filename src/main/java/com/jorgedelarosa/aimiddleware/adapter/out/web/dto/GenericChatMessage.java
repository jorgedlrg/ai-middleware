package com.jorgedelarosa.aimiddleware.adapter.out.web.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * @author jorge
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GenericChatMessage(String role, String content, String thinking) {}
