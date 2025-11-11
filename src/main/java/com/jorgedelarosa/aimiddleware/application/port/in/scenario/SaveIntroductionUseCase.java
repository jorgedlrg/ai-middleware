package com.jorgedelarosa.aimiddleware.application.port.in.scenario;

import java.util.Optional;
import java.util.UUID;

/**
 * @author jorge
 */
public interface SaveIntroductionUseCase {
  public UUID execute(Command cmd);

  public record Command(
      UUID scenario,
      UUID introduction,
      String spokenText,
      Optional<String> thoughtText,
      Optional<String> actionText,
      UUID performer,
      UUID context) {}
}
