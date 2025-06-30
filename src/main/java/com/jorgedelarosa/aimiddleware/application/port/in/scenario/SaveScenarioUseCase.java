package com.jorgedelarosa.aimiddleware.application.port.in.scenario;

import java.util.UUID;

/**
 * @author jorge
 */
public interface SaveScenarioUseCase {
  public UUID execute(Command cmd);

  public record Command(UUID id, String name) {}

  public record ContextDto(UUID id, String name, String physicalDescription) {}

}
