package com.jorgedelarosa.aimiddleware.adapter.out.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public record ComfyUiPromptResponse(
    @JsonProperty("prompt_id") String promptId,
    int number,
    Map<String, NodeError> nodeErrors) {
  public record NodeError(Object[] errors, String[] dependentOutputs, String classType) {}
}