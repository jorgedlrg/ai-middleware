package com.jorgedelarosa.aimiddleware.application.port.in.session;

import com.jorgedelarosa.aimiddleware.domain.session.Performance;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author jorge
 */
public interface CreateSessionUseCase {
public UUID execute(Command cmd);

  public record Command(UUID scenario, UUID currentContext, List<Performance> performances, Locale locale) {}
}
