package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.ComfyUiOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GenerateActorPortraitUseCaseImpl implements GenerateActorPortraitUseCase {

  private static final String WORKFLOW_TEMPLATE_PATH = "workflows/portrait.json";
  private static final String RELATIVE_PATH_TEMPLATE = "actors/%s/portrait.png";

  private final GetActorByIdOutPort getActorByIdOutPort;
  private final ComfyUiOutPort comfyUiOutPort;

  private final String workflowTemplate;
  private final ObjectMapper objectMapper;

  public GenerateActorPortraitUseCaseImpl(
      GetActorByIdOutPort getActorByIdOutPort, ComfyUiOutPort comfyUiOutPort) {
    this.getActorByIdOutPort = getActorByIdOutPort;
    this.comfyUiOutPort = comfyUiOutPort;
    this.objectMapper = new ObjectMapper();
    try {
      this.workflowTemplate =
          new String(
              new ClassPathResource(WORKFLOW_TEMPLATE_PATH).getInputStream().readAllBytes(),
              StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load workflow template: " + WORKFLOW_TEMPLATE_PATH, e);
    }
  }

  @Override
  public String execute(Command cmd) {
    var actor =
        getActorByIdOutPort
            .query(cmd.actorId())
            .orElseThrow(() -> new RuntimeException("Actor not found: " + cmd.actorId()));

    var description = actor.getPhysicalDescription();
    var seed = ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE);
    var workflowJson = workflowTemplate
        .replace("{{PHYSICAL_DESCRIPTION}}", description)
        .replace("{{SEED}}", String.valueOf(seed));

    try {
      Map<String, Object> workflow =
          objectMapper.readValue(workflowJson, new TypeReference<Map<String, Object>>() {});
      var relativePath = String.format(RELATIVE_PATH_TEMPLATE, cmd.actorId());
      var promptId = comfyUiOutPort.queuePrompt(workflow, relativePath);
      log.info("Queued portrait generation for actor {}: promptId={}", cmd.actorId(), promptId);
      return promptId;
    } catch (IOException e) {
      throw new RuntimeException("Failed to parse workflow JSON", e);
    }
  }
}
