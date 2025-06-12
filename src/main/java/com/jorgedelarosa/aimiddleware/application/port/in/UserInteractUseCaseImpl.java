package com.jorgedelarosa.aimiddleware.application.port.in;

import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
@Transactional
public class UserInteractUseCaseImpl implements UserInteractUseCase {

  private final GetScenarioByIdOutPort getScenarioByIdOutPort;
  private final GetSessionByIdOutPort getSessionByIdOutPort;
  private final SaveSessionOutPort saveSessionOutPort;

  @Override
  public void execute(Command cmd) {
    Session session = getSessionByIdOutPort.query(cmd.session()).orElseThrow();

    Scenario scenario = getScenarioByIdOutPort.query(session.getScenario()).orElseThrow(); // FIXME

    // TODO: this should come from the session, probably.
    Role role = scenario.getRoles().getFirst();

    session.interact(cmd.text(), role.getId(), true);

    saveSessionOutPort.save(session);
  }
}
