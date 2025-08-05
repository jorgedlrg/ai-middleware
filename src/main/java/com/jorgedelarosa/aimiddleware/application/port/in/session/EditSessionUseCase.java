package com.jorgedelarosa.aimiddleware.application.port.in.session;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author jorge
 */
public interface EditSessionUseCase {
  public void execute(Command cmd);

  public record Command(
      UUID session,
      UUID currentContext,
      List<PerformanceDto> performances,
      Locale locale) {}

  public record PerformanceDto(UUID actor, UUID role) {}
}
