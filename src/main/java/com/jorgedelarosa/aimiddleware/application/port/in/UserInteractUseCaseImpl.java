package com.jorgedelarosa.aimiddleware.application.port.in;

import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
import com.jorgedelarosa.aimiddleware.domain.Actor;
import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.UUID;
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
  private final GetActorByIdOutPort getActorByIdOutPort;
  private final SaveSessionOutPort saveSessionOutPort;

  @Override
  public void execute(Command cmd) {

    Session session =
        getSessionByIdOutPort
            .query(UUID.fromString("7376f89d-4ca7-423b-95f1-e29a8832ec4a"))
            .orElseThrow(); // FIXME

    Scenario scenario = getScenarioByIdOutPort.query(session.getScenario()).orElseThrow(); // FIXME

    // TODO: this should come from the session, probably.
    Role role = scenario.getRoles().getFirst();

    // TODO: Also, the actor might come from the assigned actor to the role in the session.
    Actor user =
        getActorByIdOutPort
            .query(UUID.fromString("857fa610-b987-454c-96c3-bbf5354f13a0"))
            .orElseThrow();

    session.interact(cmd.text(), role, user, true);

    saveSessionOutPort.save(session);
  }
}
