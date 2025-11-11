package com.jorgedelarosa.aimiddleware.application.port.in.session;

import java.util.Optional;
import java.util.UUID;

/**
 * @author jorge
 */
public interface UserInteractUseCase {

  public void execute(Command cmd);

  public record Command(UUID session, UUID role, String text, Optional<UUID> autoreplyRole) {}
}
