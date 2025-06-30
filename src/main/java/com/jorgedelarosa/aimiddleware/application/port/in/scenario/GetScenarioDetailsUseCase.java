package com.jorgedelarosa.aimiddleware.application.port.in.scenario;

import java.util.List;
import java.util.UUID;

/**
 * @author jorge
 */
public interface GetScenarioDetailsUseCase {
  public ScenarioDto execute(Command cmd);

  public record Command(UUID scenarioId) {}

  public record ScenarioDto(UUID id, String name, List<ContextDto> contexts, List<RoleDto> roles) {}

  public record ContextDto(UUID id, String name, String physicalDescription) {}

  public record RoleDto(UUID id, String name, String details) {}
}
