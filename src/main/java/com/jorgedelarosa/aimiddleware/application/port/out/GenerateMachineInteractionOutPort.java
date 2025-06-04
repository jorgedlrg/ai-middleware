package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.scenario.Context;
import com.jorgedelarosa.aimiddleware.domain.session.Session;

/**
 * @author jorge
 */
public interface GenerateMachineInteractionOutPort {

  public MachineResponse execute(Command cmd);

  // TODO this needs refinement. Probably I won't send the whole session. This is in discovery stage
  public record Command(Session session, Context currentContext) {}
  ;

  // TODO refine this
  public record MachineResponse(String text) {}
  ;
}
