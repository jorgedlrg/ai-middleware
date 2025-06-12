package com.jorgedelarosa.aimiddleware.application.port.in;

import com.jorgedelarosa.aimiddleware.application.port.out.GenerateMachineInteractionOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorListByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
import com.jorgedelarosa.aimiddleware.domain.Actor;
import com.jorgedelarosa.aimiddleware.domain.scenario.Context;
import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.List;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
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
  private final GetActorListByIdOutPort getActorListByIdOutPort;
  private final SaveSessionOutPort saveSessionOutPort;
  private final GenerateMachineInteractionOutPort generateMachineInteractionOutPort;

  @Override
  public void execute(Command cmd) {
    Session session = getSessionByIdOutPort.query(cmd.session()).orElseThrow();
    Scenario scenario = getScenarioByIdOutPort.query(session.getScenario()).orElseThrow();
    Context currentContext =
        scenario.getContexts().stream()
            .filter(e -> e.getId().equals(session.getCurrentContext()))
            .findFirst()
            .orElseThrow();

    Role role = scenario.getRoles().getLast(); // FIXME

    Actor actingActor =
        getActorByIdOutPort.query(session.getFeaturedActor(role.getId()).get()).orElseThrow();

    List<Actor> featuredActors = getActorListByIdOutPort.query(session.getFeaturedActors());

    List<GenerateMachineInteractionOutPort.PreviousMessage> previousMessages =
        session.getInteractions().stream()
            .map(
                (e) ->
                    MessageMapper.INSTANCE.toMessage(
                        getActorByIdOutPort
                            .query(session.getFeaturedActor(e.getRole()).get())
                            .orElseThrow()
                            .getName(),
                        e.getSpokenText()))
            .toList();
    GenerateMachineInteractionOutPort.MachineResponse response =
        generateMachineInteractionOutPort.execute(
            new GenerateMachineInteractionOutPort.Command(
                session, currentContext, featuredActors, actingActor, previousMessages));
    session.interact(response.text(), role.getId(), false);

    saveSessionOutPort.save(session);
  }

  @Mapper
  public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    default GenerateMachineInteractionOutPort.PreviousMessage toMessage(
        String actorName, String message) {
      return new GenerateMachineInteractionOutPort.PreviousMessage(actorName, message);
    }
  }
}
