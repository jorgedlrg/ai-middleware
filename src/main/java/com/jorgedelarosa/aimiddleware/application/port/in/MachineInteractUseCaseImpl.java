package com.jorgedelarosa.aimiddleware.application.port.in;

import com.jorgedelarosa.aimiddleware.application.port.out.GenerateMachineInteractionOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.Interaction;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
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
  private final SaveSessionOutPort saveSessionOutPort;
  private final GenerateMachineInteractionOutPort generateMachineInteractionOutPort;

  @Override
  public void execute(Command cmd) {
    // TODO: POC code
    Scenario scenario =
        getScenarioByIdOutPort.query(
            UUID.fromString("7376f89d-4ca7-423b-95f1-e29a8832ec4a")); // FIXME

    Session session =
        getSessionByIdOutPort.query(
            UUID.fromString("7376f89d-4ca7-423b-95f1-e29a8832ec4a")); // FIXME

    //TODO: I need to think about this... quite probably the outport shouldn't generate an interaction and it needs to be built inside the session aggregate.
    Interaction interaction = generateMachineInteractionOutPort.execute(new GenerateMachineInteractionOutPort.Command());
    session.interact(interaction.getSpokenText(), scenario.getRoles().getLast());
    
    saveSessionOutPort.save(session);
  }
}
