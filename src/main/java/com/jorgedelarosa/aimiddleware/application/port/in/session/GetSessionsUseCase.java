package com.jorgedelarosa.aimiddleware.application.port.in.session;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * @author jorge
 */
public interface GetSessionsUseCase {

  public List<SessionDto> execute(Command cmd);

  public record Command() {}

  public record SessionDto(
      UUID session,
      String scenario,
      Instant lastActivity,
      int interactionCount,
      List<String> participantNames) {}
}
