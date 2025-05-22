package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.session.Interaction;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
public class MachineInteractionAdapter implements GenerateMachineInteractionOutPort {

  @Override
  public Interaction execute(Command cmd) {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }
}
