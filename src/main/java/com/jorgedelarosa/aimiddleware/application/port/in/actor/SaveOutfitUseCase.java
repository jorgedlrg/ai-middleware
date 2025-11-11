package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import java.util.UUID;

/**
 * @author jorge
 */
public interface SaveOutfitUseCase {
  public UUID execute(Command cmd);

  public record Command(UUID id, String name, String description) {}
}
