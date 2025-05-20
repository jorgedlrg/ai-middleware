package com.jorgedelarosa.aimiddleware.application.port.in;

import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
public class UserInteractUseCaseImpl implements UserInteractUseCase {

  @Override
  public void execute(Command cmd) {
    // TODO: POC code
    Scenario scenario = new Scenario(new Role(), new Role());

    Session session = new Session(scenario);

    session.interact(cmd.text(), scenario.getRoles().getFirst());
  }
}
