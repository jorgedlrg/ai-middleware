package com.jorgedelarosa.aimiddleware.adapter.out.web.dto;

import java.util.List;

/**
 * @author jorge
 */
public record OllamaChatRequest(String model, List<OllamaChatMessage> messages, boolean stream) {}
