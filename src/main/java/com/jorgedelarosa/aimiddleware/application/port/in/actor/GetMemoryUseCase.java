package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * @author jorge
 */
public interface GetMemoryUseCase {
  public MemoryDto execute(Command cmd);

  public record Command(UUID actorId) {}

  public record MemoryDto(UUID actorId, List<MemoryFragmentDto> fragments) {}

  public record MemoryFragmentDto(String text, Instant timestamp, boolean enabled) {}
}
