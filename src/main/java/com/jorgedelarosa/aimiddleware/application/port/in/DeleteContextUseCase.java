package com.jorgedelarosa.aimiddleware.application.port.in;

import java.util.UUID;

/**
 * @author jorge
 */
public interface DeleteContextUseCase {
  public void execute(Command cmd);

  public record Command(UUID scenarioId, UUID contextId) {}
}
