package com.jorgedelarosa.aimiddleware.application.port.in.scenario;

import java.util.UUID;

/**
 * @author jorge
 */
public interface SaveRoleUseCase {
  public UUID execute(Command cmd);

  public record Command(UUID scenario, UUID id, String name, String details) {}
}
