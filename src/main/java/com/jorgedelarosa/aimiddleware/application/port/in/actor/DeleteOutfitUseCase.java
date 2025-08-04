package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import java.util.UUID;

/**
 * @author jorge
 */
public interface DeleteOutfitUseCase {
  public void execute(Command cmd);

  public record Command(UUID id) {}
}
