package com.jorgedelarosa.aimiddleware.adapter.out.web.dto;

public record ComfyUiWebSocketMessage(String type, WebSocketData data) {
  public record WebSocketData(String promptId, String node, Object extraInfo, Object promptNode) {}
}