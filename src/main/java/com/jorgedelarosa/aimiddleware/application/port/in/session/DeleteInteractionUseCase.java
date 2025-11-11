package com.jorgedelarosa.aimiddleware.application.port.in.session;

import java.util.UUID;

/**
 * @author jorge
 */
public interface DeleteInteractionUseCase {
  public void execute(Command cmd);

  public record Command(UUID sessionId, UUID interactionId) {}
}
