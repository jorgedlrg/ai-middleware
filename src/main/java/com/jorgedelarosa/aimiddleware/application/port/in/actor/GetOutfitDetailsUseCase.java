package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import java.util.UUID;

/**
 * @author jorge
 */
public interface GetOutfitDetailsUseCase {
  public OutfitDto execute(Command cmd);

  public record Command(UUID id) {}

  public record OutfitDto(UUID id, String name, String description) {}
}
