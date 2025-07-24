package com.jorgedelarosa.aimiddleware.application.port.in.user;

import java.util.UUID;

/**
 * @author jorge
 */
public interface UpdateUserSettingsUseCase {
  public void execute(Command cmd);

  public record Command(
      UUID user,
      String textgenProvider,
      String openrouterApikey,
      String openrouterModel,
      String ollamaHost,
      String ollamaModel,
      boolean actionsEnabled,
      boolean moodEnabled,
      boolean thoughtsEnabled) {}
}
