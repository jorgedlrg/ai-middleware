package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.Actor;
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
      List<PreviousMessage> previousMessages) {}

  public record PreviousMessage(String actorName, String message) {}

  public record MachineResponse(String text) {}
}
