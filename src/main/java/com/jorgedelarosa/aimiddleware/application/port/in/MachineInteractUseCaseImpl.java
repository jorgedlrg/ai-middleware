package com.jorgedelarosa.aimiddleware.application.port.in;

import com.jorgedelarosa.aimiddleware.application.port.out.GenerateMachineInteractionOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
import com.jorgedelarosa.aimiddleware.domain.Actor;
import com.jorgedelarosa.aimiddleware.domain.scenario.Context;
import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
public class MachineInteractUseCaseImpl implements MachineInteractUseCase {

  private final GetScenarioByIdOutPort getScenarioByIdOutPort;
  private final GetSessionByIdOutPort getSessionByIdOutPort;
  private final GetActorByIdOutPort getActorByIdOutPort;
  private final SaveSessionOutPort saveSessionOutPort;
  private final GenerateMachineInteractionOutPort generateMachineInteractionOutPort;

  @Override
  public void execute(Command cmd) {
    Session session =
        getSessionByIdOutPort
            .query(UUID.fromString("7376f89d-4ca7-423b-95f1-e29a8832ec4a"))
            .orElseThrow(); // FIXME

    Scenario scenario = getScenarioByIdOutPort.query(session.getScenario()).orElseThrow(); // FIXME
    Context currentContext =
        scenario
            .getContexts()
            .getFirst(); // FIXME temporary. this might be in the session, probably.

    Role role = scenario.getRoles().getLast();

    // TODO: Also, the actor might come from the assigned actor to the role in the session.
    Actor user =
        getActorByIdOutPort
            .query(UUID.fromString("857fa610-b987-454c-96c3-bbf5354f13a0"))
            .orElseThrow();
    Actor machine =
        getActorByIdOutPort
            .query(UUID.fromString("caa30e65-1886-4366-bfb7-f415af9f4a40"))
            .orElseThrow();
    GenerateMachineInteractionOutPort.MachineResponse response =
        generateMachineInteractionOutPort.execute(
            new GenerateMachineInteractionOutPort.Command(
                session, currentContext, List.of(user, machine), machine));
    session.interact(response.text(), role, machine, false);

    saveSessionOutPort.save(session);
  }
}
