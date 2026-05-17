package com.jorgedelarosa.aimiddleware.application.port.mapper;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.SettingsEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.UserEntity;
import com.jorgedelarosa.aimiddleware.application.port.in.user.GetUserSettingsUseCase;
import com.jorgedelarosa.aimiddleware.domain.user.Settings;
import com.jorgedelarosa.aimiddleware.domain.user.User;
import java.util.UUID;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-17T18:25:57+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Arch Linux)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public UserEntity toEntity(User dom) {
        if ( dom == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setId( dom.getId() );
        userEntity.setEmail( dom.getEmail() );

        return userEntity;
    }

    @Override
    public SettingsEntity toSettingsEntity(Settings dom) {
        if ( dom == null ) {
            return null;
        }

        SettingsEntity settingsEntity = new SettingsEntity();

        settingsEntity.setUserid( dom.getId() );
        settingsEntity.setTextgenProvider( dom.getTextgenProvider() );
        settingsEntity.setOpenrouterApikey( dom.getOpenrouterApikey() );
        settingsEntity.setOpenrouterModel( dom.getOpenrouterModel() );
        settingsEntity.setOllamaHost( dom.getOllamaHost() );
        settingsEntity.setOllamaModel( dom.getOllamaModel() );
        settingsEntity.setActionsEnabled( dom.isActionsEnabled() );
        settingsEntity.setMoodEnabled( dom.isMoodEnabled() );
        settingsEntity.setThoughtsEnabled( dom.isThoughtsEnabled() );
        settingsEntity.setActionsReasoning( dom.isActionsReasoning() );
        settingsEntity.setSpeechReasoning( dom.isSpeechReasoning() );
        settingsEntity.setThoughtsReasoning( dom.isThoughtsReasoning() );

        return settingsEntity;
    }

    @Override
    public GetUserSettingsUseCase.SettingsDto toSettingsDto(Settings dom) {
        if ( dom == null ) {
            return null;
        }

        UUID user = null;
        String textgenProvider = null;
        String openrouterApikey = null;
        String openrouterModel = null;
        String ollamaHost = null;
        String ollamaModel = null;
        boolean actionsEnabled = false;
        boolean moodEnabled = false;
        boolean thoughtsEnabled = false;
        boolean actionsReasoning = false;
        boolean speechReasoning = false;
        boolean thoughtsReasoning = false;

        user = dom.getId();
        textgenProvider = dom.getTextgenProvider();
        openrouterApikey = dom.getOpenrouterApikey();
        openrouterModel = dom.getOpenrouterModel();
        ollamaHost = dom.getOllamaHost();
        ollamaModel = dom.getOllamaModel();
        actionsEnabled = dom.isActionsEnabled();
        moodEnabled = dom.isMoodEnabled();
        thoughtsEnabled = dom.isThoughtsEnabled();
        actionsReasoning = dom.isActionsReasoning();
        speechReasoning = dom.isSpeechReasoning();
        thoughtsReasoning = dom.isThoughtsReasoning();

        GetUserSettingsUseCase.SettingsDto settingsDto = new GetUserSettingsUseCase.SettingsDto( user, textgenProvider, openrouterApikey, openrouterModel, ollamaHost, ollamaModel, actionsEnabled, moodEnabled, thoughtsEnabled, actionsReasoning, speechReasoning, thoughtsReasoning );

        return settingsDto;
    }
}
