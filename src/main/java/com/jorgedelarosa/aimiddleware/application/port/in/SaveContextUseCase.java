package com.jorgedelarosa.aimiddleware.application.port.in;

import java.util.UUID;

/**
 * @author jorge
 */
public interface SaveContextUseCase {

  public UUID execute(Command cmd);

  public record Command(UUID scenario, UUID id, String name, String physicalDescription) {}
}
