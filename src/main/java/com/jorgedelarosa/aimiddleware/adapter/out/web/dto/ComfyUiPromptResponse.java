package com.jorgedelarosa.aimiddleware.adapter.out.web.dto;

import java.util.Map;

public record ComfyUiPromptResponse(
    String promptId,
    int number,
    Map<String, NodeError> nodeErrors
) {
    public record NodeError(
        Object[] errors,
        String[] dependentOutputs,
        String classType
    ) {}
}