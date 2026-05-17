package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.actor.MemoryFragment;
import com.jorgedelarosa.aimiddleware.domain.user.Settings;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-17T18:25:57+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.3 (Arch Linux)"
)
public class GenerateMachineInteractionOutPort$TextGenMapperImpl implements GenerateMachineInteractionOutPort.TextGenMapper {

    @Override
    public GenerateMachineInteractionOutPort.TextGenSettingsDto toSettingsEntity(Settings dom) {
        if ( dom == null ) {
            return null;
        }

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

        GenerateMachineInteractionOutPort.TextGenSettingsDto textGenSettingsDto = new GenerateMachineInteractionOutPort.TextGenSettingsDto( textgenProvider, openrouterApikey, openrouterModel, ollamaHost, ollamaModel, actionsEnabled, moodEnabled, thoughtsEnabled, actionsReasoning, speechReasoning, thoughtsReasoning );

        return textGenSettingsDto;
    }

    @Override
    public GenerateMachineInteractionOutPort.MemoryFragmentDto toDto(MemoryFragment dom) {
        if ( dom == null ) {
            return null;
        }

        String text = null;

        text = dom.getText();

        GenerateMachineInteractionOutPort.MemoryFragmentDto memoryFragmentDto = new GenerateMachineInteractionOutPort.MemoryFragmentDto( text );

        return memoryFragmentDto;
    }
}
