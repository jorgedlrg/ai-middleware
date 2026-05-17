package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface GetActorsUseCase {

  public List<ActorDto> execute();

  public record ActorDto(UUID id, String name, Instant updatedAt) {}
}
