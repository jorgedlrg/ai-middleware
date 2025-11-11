package com.jorgedelarosa.aimiddleware.application.port.in.session;

import java.util.UUID;

/**
 * @author jorge
 */
public interface DeleteSessionWithScenarioUseCase {
  public void execute(Command cmd);

  public record Command(UUID scenario) {}
}
