package com.jorgedelarosa.aimiddleware.application.port.in.session;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author jorge
 */
public interface GetSessionDetailsUseCase {

  public SessionDto execute(Command cmd);

  public record Command(UUID session) {}

  public record SessionDto(
      UUID session,
      UUID currentContext,
      Locale locale,
      List<PerformanceDto> performances,
      List<InteractionDto> interactions) {}

  public record PerformanceDto(UUID actor, UUID role, String actorName, String roleName) {}

  public record InteractionDto(UUID id, Instant timestamp, String actorName, String spokenText) {}
}
