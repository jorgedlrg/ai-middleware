package com.jorgedelarosa.aimiddleware.application.port.in;

import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.Session;

/**
 * @author jorge
 */
public class MachineInteractUseCaseImpl implements MachineInteractUseCase {

  @Override
  public void execute(Command cmd) {
    // TODO: POC code
    Scenario scenario = new Scenario(new Role(), new Role());

    Session session = new Session(scenario);

    String text = "TODO Generate text with AI client";
    session.interact(text, scenario.getRoles().getLast());
  }
}
