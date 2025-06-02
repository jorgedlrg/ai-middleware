package com.jorgedelarosa.aimiddleware.application.port.in;

import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
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
  private final SaveSessionOutPort saveSessionOutPort;

  @Override
  public void execute(Command cmd) {

    Session session =
        getSessionByIdOutPort
            .query(UUID.fromString("7376f89d-4ca7-423b-95f1-e29a8832ec4a"))
            .orElseThrow(); // FIXME

    Scenario scenario = getScenarioByIdOutPort.query(session.getScenario()).orElseThrow(); // FIXME

    session.interact(cmd.text(), scenario.getRoles().getFirst(), true);

    saveSessionOutPort.save(session);
  }
}
