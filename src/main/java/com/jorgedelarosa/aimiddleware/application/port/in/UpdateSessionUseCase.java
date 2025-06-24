package com.jorgedelarosa.aimiddleware.application.port.in;

import java.util.Locale;
import java.util.UUID;

/**
 * @author jorge
 */
public interface UpdateSessionUseCase {

  public void execute(Command cmd);

  public record Command(UUID id, Locale locale) {}
}
