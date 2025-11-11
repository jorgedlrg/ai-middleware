package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import java.util.List;
import java.util.UUID;

/**
 * @author jorge
 */
public interface GetOutfitsUseCase {
  public List<OutfitDto> execute();

  public record OutfitDto(UUID id, String name, String description) {}
}
