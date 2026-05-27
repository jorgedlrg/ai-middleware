package com.jorgedelarosa.aimiddleware.adapter.out.web.dto;

import java.util.List;
import java.util.Map;

public record ComfyUiHistoryResponse(
    Map<String, PromptHistory> history
) {
    public record PromptHistory(
        List<OutputNode> outputs
    ) {}

    public record OutputNode(
        List<ImageOutput> images
    ) {}

    public record ImageOutput(
        String filename,
        String subfolder,
        String type
    ) {}
}