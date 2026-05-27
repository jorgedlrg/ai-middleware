package com.jorgedelarosa.aimiddleware.adapter.out.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ComfyUiWebSocketMessage(String type, WebSocketData data) {
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record WebSocketData(
      @JsonProperty("prompt_id") String promptId,
      String node,
      Object extraInfo,
      Object promptNode) {}
}