package com.jorgedelarosa.aimiddleware.adapter.out.web.dto;

import java.util.Map;

public record ComfyUiPromptRequest(
    Map<String, Object> prompt, String clientId, ExtraData extraData, boolean front, int number) {
  public record ExtraData(Map<String, Object> extraPnginfo) {}
}