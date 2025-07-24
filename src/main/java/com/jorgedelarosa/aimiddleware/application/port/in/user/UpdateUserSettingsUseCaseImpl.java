package com.jorgedelarosa.aimiddleware.application.port.in.user;

import com.jorgedelarosa.aimiddleware.application.port.out.GetUserByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveUserOutPort;
import com.jorgedelarosa.aimiddleware.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
@Transactional
public class UpdateUserSettingsUseCaseImpl implements UpdateUserSettingsUseCase {

  private final GetUserByIdOutPort getUserByIdOutPort;
  private final SaveUserOutPort saveUserOutPort;

  @Override
  public void execute(Command cmd) {
    User user = getUserByIdOutPort.query(cmd.user()).orElseThrow();
    user.getSettings().setActionsEnabled(cmd.actionsEnabled());
    user.getSettings().setMoodEnabled(cmd.moodEnabled());
    user.getSettings().setOllamaHost(cmd.ollamaHost());
    user.getSettings().setOllamaModel(cmd.ollamaModel());
    user.getSettings().setOpenrouterApikey(cmd.openrouterApikey());
    user.getSettings().setOpenrouterModel(cmd.openrouterModel());
    user.getSettings().setTextgenProvider(cmd.textgenProvider());
    user.getSettings().setThoughtsEnabled(cmd.thoughtsEnabled());

    saveUserOutPort.save(user);
  }
}
