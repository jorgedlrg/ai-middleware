package com.jorgedelarosa.aimiddleware.application.port.in.session;

import java.util.Locale;
import java.util.UUID;

/**
 * @author jorge
 */
public interface UpdateSessionLocaleUseCase {

  public void execute(Command cmd);

  public record Command(UUID id, Locale locale) {}
}
