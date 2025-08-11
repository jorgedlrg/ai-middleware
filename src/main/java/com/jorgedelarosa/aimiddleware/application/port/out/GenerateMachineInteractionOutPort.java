package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.scenario.Context;
import com.jorgedelarosa.aimiddleware.domain.user.Settings;
import java.util.List;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author jorge
 */
public interface GenerateMachineInteractionOutPort {

  public MachineResponse execute(Command cmd);

  public record Command(
      String scenarioDescription,
      Context currentContext,
      Actor you,
      List<PerformanceDto> performances,
      List<PreviousMessage> previousMessages,
      String replyLanguage,
      TextGenSettingsDto settings) {}

  public record PerformanceDto(
      String roleName,
      String actorName,
      String physicalDescription,
      Optional<String> currentOutfit,
      Optional<String> personality,
      String roleDescription) {}

  public record PreviousMessage(String actorName, String action, String speech) {}

  public record TextDto(String text, Optional<String> reasoning) {}

  public record MachineResponse(TextDto thoughts, TextDto action, TextDto speech, String mood) {}

  public record TextGenSettingsDto(
      String textgenProvider,
      String openrouterApikey,
      String openrouterModel,
      String ollamaHost,
      String ollamaModel,
      boolean actionsEnabled,
      boolean moodEnabled,
      boolean thoughtsEnabled,
      boolean actionsReasoning,
      boolean speechReasoning,
      boolean thoughtsReasoning) {}

  @Mapper
  public interface TextGenMapper {
    TextGenMapper INSTANCE = Mappers.getMapper(TextGenMapper.class);

    TextGenSettingsDto toSettingsEntity(Settings dom);
  }
}
