package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * @author jorge
 */
public interface SaveMemoryUseCase {

  public UUID execute(Command cmd);

  public record Command(UUID actorId, List<MemoryFragmentDto> fragments) {}

  public record MemoryFragmentDto(UUID id, String text, Instant timestamp, boolean enabled) {}
}
