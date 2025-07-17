package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.scenario.Context;
import java.util.List;
import java.util.Optional;

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
      String replyLanguage) {}

  public record PerformanceDto(
      String roleName,
      String actorName,
      String physicalDescription,
      Optional<String> currentOutfit,
      Optional<String> personality,
      String roleDescription) {}

  public record PreviousMessage(String actorName, String action, String speech) {}

  public record MachineResponse(String thoughts, String action, String speech, String mood) {}
}
