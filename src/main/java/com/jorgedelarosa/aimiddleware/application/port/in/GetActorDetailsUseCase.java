package com.jorgedelarosa.aimiddleware.application.port.in;

import java.util.UUID;

/**
 * @author jorge
 */
public interface GetActorDetailsUseCase {
  public ActorDto execute(Command cmd);

  public record Command(UUID actorId) {}

  public record ActorDto(UUID id, String name, String physicalDescription) {}
}
