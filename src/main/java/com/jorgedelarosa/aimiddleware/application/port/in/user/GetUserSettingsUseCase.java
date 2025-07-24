package com.jorgedelarosa.aimiddleware.application.port.in.user;

import java.util.UUID;

/**
 * @author jorge
 */
public interface GetUserSettingsUseCase {
  public SettingsDto execute(Command cmd);

  public record Command(UUID user) {}

  public record SettingsDto(
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
