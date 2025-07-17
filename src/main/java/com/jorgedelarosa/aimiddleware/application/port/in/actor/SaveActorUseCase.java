package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import java.util.UUID;

/**
 * @author jorge
 */
public interface SaveActorUseCase {
  public UUID execute(Command cmd);

  public record Command(UUID id, String name, String physicalDescription, String personality, byte[] portrait, UUID outfit) {}
}
