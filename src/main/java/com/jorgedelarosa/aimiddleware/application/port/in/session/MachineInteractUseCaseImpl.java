package com.jorgedelarosa.aimiddleware.application.port.in.session;

import com.jorgedelarosa.aimiddleware.application.port.out.GenerateMachineInteractionOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorListByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.scenario.Context;
import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.Mood;
import com.jorgedelarosa.aimiddleware.domain.session.Performance;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
@Transactional
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

    Actor actingActor =
        getActorByIdOutPort.query(session.getFeaturedActor(cmd.role()).get()).orElseThrow();

    List<GenerateMachineInteractionOutPort.PreviousMessage> previousMessages =
        session.getCurrentInteractions().stream()
            .map(
                (e) ->
                    MessageMapper.INSTANCE.toMessage(
                        getActorByIdOutPort
                            .query(session.getFeaturedActor(e.getRole()).get())
                            .orElseThrow()
                            .getName(),
                        e.getSpokenText()))
            .toList();

    List<Actor> featuredActors = getActorListByIdOutPort.query(session.getFeaturedActors());
    List<GenerateMachineInteractionOutPort.PerformanceDto> performances =
        session.getPerformances().stream()
            .map(e -> MessageMapper.INSTANCE.toDto(e, scenario, featuredActors))
            .toList();

    GenerateMachineInteractionOutPort.MachineResponse response =
        generateMachineInteractionOutPort.execute(
            new GenerateMachineInteractionOutPort.Command(
                scenario.getDescription(),
                currentContext,
                actingActor,
                performances,
                previousMessages,
                session.getLocale().getDisplayLanguage(Locale.ENGLISH)));
    session.interact(
        response.thoughts(),
        response.speech(),
        cmd.role(),
        Optional.of(Mood.valueOf(response.mood().toUpperCase())));

    saveSessionOutPort.save(session);
  }

  @Mapper
  public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    default GenerateMachineInteractionOutPort.PreviousMessage toMessage(
        String actorName, String message) {
      return new GenerateMachineInteractionOutPort.PreviousMessage(actorName, message);
    }

    default GenerateMachineInteractionOutPort.PerformanceDto toDto(
        Performance performance, Scenario scenario, List<Actor> featuredActors) {
      Role role =
          scenario.getRoles().stream()
              .filter(r -> r.getId().equals(performance.getRole()))
              .findFirst()
              .orElseThrow();
      Actor actor =
          featuredActors.stream()
              .filter(a -> a.getId().equals(performance.getActor()))
              .findFirst()
              .orElseThrow();
      return new GenerateMachineInteractionOutPort.PerformanceDto(
          role.getName(),
          actor.getName(),
          actor.getPhysicalDescription(),
          actor.getCurrentOutfit().map(e -> e.getDescription()),
          actor.getMind().map(e -> e.getPersonality()),
          role.getDetails());
    }
  }
}
