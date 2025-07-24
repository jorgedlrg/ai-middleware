package com.jorgedelarosa.aimiddleware.application.port.mapper;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.SettingsEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.UserEntity;
import com.jorgedelarosa.aimiddleware.application.port.in.user.GetUserSettingsUseCase.SettingsDto;
import com.jorgedelarosa.aimiddleware.domain.user.Settings;
import com.jorgedelarosa.aimiddleware.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author jorge
 */
@Mapper
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  UserEntity toEntity(User dom);

  @Mapping(target = "userid", source = "id")
  SettingsEntity toSettingsEntity(Settings dom);

  default Settings toSettings(SettingsEntity se) {
    return Settings.restore(
        se.getUserid(),
        se.getTextgenProvider(),
        se.getOpenrouterApikey(),
        se.getOpenrouterModel(),
        se.getOllamaHost(),
        se.getOllamaModel(),
        se.isActionsEnabled(),
        se.isMoodEnabled(),
        se.isThoughtsEnabled());
  }

  @Mapping(target = "user", source = "id")
  SettingsDto toSettingsDto(Settings dom);
}
