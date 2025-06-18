package com.jorgedelarosa.aimiddleware.application.port.in;

import java.util.List;
import java.util.UUID;

/**
 * @author jorge
 */
public interface GetActorsUseCase {

  public List<ActorDto> execute(Command cmd);

  // TODO: find by user
  public record Command() {}

  public record ActorDto(UUID id, String name) {}
}
