package com.jorgedelarosa.aimiddleware.application.port.in.scenario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author jorge
 */
public interface GetScenarioDetailsUseCase {
  public ScenarioDto execute(Command cmd);

  public record Command(UUID scenarioId) {}

  public record ScenarioDto(
      UUID id,
      String name,
      String description,
      List<ContextDto> contexts,
      List<RoleDto> roles,
      List<IntroductionDto> introductions) {}

  public record ContextDto(UUID id, String name, String physicalDescription) {}

  public record RoleDto(UUID id, String name, String details) {}

  public record IntroductionDto(
      UUID id,
      String spokenText,
      Optional<String> thoughtText,
      Optional<String> actionText,
      UUID performer,
      String performerName,
      UUID context,
      String contextName) {}
}
