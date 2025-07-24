package com.jorgedelarosa.aimiddleware.application.port.in.user;

import com.jorgedelarosa.aimiddleware.application.port.mapper.UserMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.GetUserByIdOutPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
public class GetUserSettingsUseCaseImpl implements GetUserSettingsUseCase {

  private final GetUserByIdOutPort getUserByIdOutPort;

  @Override
  public SettingsDto execute(Command cmd) {
    return getUserByIdOutPort
        .query(cmd.user())
        .map(e -> UserMapper.INSTANCE.toSettingsDto(e.getSettings()))
        .orElseThrow();
  }
}
