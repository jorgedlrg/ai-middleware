package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.session.Interaction;

/**
 * @author jorge
 */
public interface GenerateMachineInteractionOutPort {

  //FIXME: probably shouldn't return an Interaction
public Interaction execute(Command cmd);

public record Command(){};
}
