package com.jorgedelarosa.aimiddleware.adapter.out.web;

import com.jorgedelarosa.aimiddleware.application.port.out.GenerateMachineInteractionOutPort;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
public class MachineInteractionAdapter implements GenerateMachineInteractionOutPort {

  @Override
  public MachineResponse execute(Command cmd) {
    return new MachineResponse("TODO");
  }
}
