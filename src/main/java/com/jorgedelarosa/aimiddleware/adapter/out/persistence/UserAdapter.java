package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.SettingsRepository;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.UserEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.UserRepository;
import com.jorgedelarosa.aimiddleware.application.port.mapper.UserMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.GetUserByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveUserOutPort;
import com.jorgedelarosa.aimiddleware.domain.user.Settings;
import com.jorgedelarosa.aimiddleware.domain.user.User;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
public class UserAdapter implements GetUserByIdOutPort, SaveUserOutPort {

  private final UserRepository userRepository;
  private final SettingsRepository settingsRepository;

  @Override
  public Optional<User> query(UUID id) {
    return userRepository.findById(id).map(e -> restoreUser(e));
  }

  @Override
  public void save(User user) {
    userRepository.save(UserMapper.INSTANCE.toEntity(user));
    settingsRepository.save(UserMapper.INSTANCE.toSettingsEntity(user.getSettings()));
  }

  private User restoreUser(UserEntity ue) {
    Settings settings =
        settingsRepository
            .findById(ue.getId())
            .map(e -> UserMapper.INSTANCE.toSettings(e))
            .orElseThrow();

    return User.restore(ue.getId(), ue.getEmail(), settings);
  }
}
