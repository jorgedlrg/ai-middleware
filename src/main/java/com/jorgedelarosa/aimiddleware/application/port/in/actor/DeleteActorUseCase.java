package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import java.util.UUID;

/**
 * @author jorge
 */
public interface DeleteActorUseCase {
  public void execute(Command cmd);

  public record Command(UUID actorId) {}
}
