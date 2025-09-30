package com.jorgedelarosa.aimiddleware.application.port.in.session;

import java.util.List;
import java.util.UUID;

/**
 * @author jorge
 */
public interface GetSessionsUseCase {

  public List<SessionDto> execute(Command cmd);

  // TODO: find by user
  public record Command() {}

  public record SessionDto(UUID session, String scenario) {}
}
