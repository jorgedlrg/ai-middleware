package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import java.util.Optional;
import java.util.UUID;

/**
 * @author jorge
 */
public interface GetActorDetailsUseCase {
  public ActorDto execute(Command cmd);

  public record Command(UUID actorId) {}

  public record ActorDto(
      UUID id,
      String name,
      String profile,
      String physicalDescription,
      Optional<MindDto> mind,
      byte[] portrait,
      Optional<UUID> currentOutfit) {}

  public record MindDto(String personality) {}
}
