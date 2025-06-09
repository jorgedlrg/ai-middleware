package com.jorgedelarosa.aimiddleware.application.port.in;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * @author jorge
 */
public interface RetrieveSessionInteractionsUseCase {

  public List<InteractionDto> execute(Command cmd);

  public record Command(UUID session) {}

  public record InteractionDto(Instant timestamp, boolean user, UUID role, UUID actor, String spokenText) {}
}
