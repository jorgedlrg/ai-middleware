package com.jorgedelarosa.aimiddleware.adapter.out.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public record ComfyUiPromptRequest(
    Map<String, Object> prompt, @JsonProperty("client_id") String clientId) {
  
}