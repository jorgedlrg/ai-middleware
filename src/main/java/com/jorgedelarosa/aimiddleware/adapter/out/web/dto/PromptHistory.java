package com.jorgedelarosa.aimiddleware.adapter.out.web.dto;

import java.util.List;
import java.util.Map;

public record PromptHistory(Map<String, OutputNode> outputs) {

  public record OutputNode(List<ImageOutput> images) {}

  public record ImageOutput(String filename, String subfolder, String type) {}
}
