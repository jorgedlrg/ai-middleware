package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.scenario.Context;
import java.util.List;

/**
 * @author jorge
 */
public interface GenerateMachineInteractionOutPort {

  public MachineResponse execute(Command cmd);

  public record Command(
      Context currentContext,
      List<Actor> actors,
      Actor you,
      List<PerformanceDto> performances,
      List<PreviousMessage> previousMessages,
      String replyLanguage) {}

  public record PreviousMessage(String actorName, String message) {}

  public record MachineResponse(String text) {}

  public record PerformanceDto(String roleName, String actorName) {}
}
