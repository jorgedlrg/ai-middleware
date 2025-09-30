package com.jorgedelarosa.aimiddleware.application.port.in.session;

import com.jorgedelarosa.aimiddleware.application.port.mapper.MessageMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.GenerateMachineInteractionOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorListByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetMemoryByActorOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetOutfitListByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetUserByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.actor.Memory;
import com.jorgedelarosa.aimiddleware.domain.actor.Outfit;
import com.jorgedelarosa.aimiddleware.domain.scenario.Context;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.InteractionText;
import com.jorgedelarosa.aimiddleware.domain.session.Mood;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import com.jorgedelarosa.aimiddleware.domain.user.User;
import java.util.List;
import java.util.Locale;
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
public class MachineInteractUseCaseImpl implements MachineInteractUseCase {

  private final UUID userId =
      UUID.fromString(
          "857fa610-b987-454c-96c3-bbf5354f13a0"); // FIXME, get this from security context.. when I
  // implement it

  private final GetScenarioByIdOutPort getScenarioByIdOutPort;
  private final GetSessionByIdOutPort getSessionByIdOutPort;
  private final GetActorByIdOutPort getActorByIdOutPort;
  private final GetActorListByIdOutPort getActorListByIdOutPort;
  private final GetOutfitListByIdOutPort getOutfitListByIdOutPort;
  private final SaveSessionOutPort saveSessionOutPort;
  private final GenerateMachineInteractionOutPort generateMachineInteractionOutPort;
  private final GetUserByIdOutPort getUserByIdOutPort;
  private final GetMemoryByActorOutPort getMemoryByActorOutPort;

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

    Memory memory = getMemoryByActorOutPort.query(actingActor.getId());

    List<GenerateMachineInteractionOutPort.PreviousMessage> previousMessages =
        session.getCurrentInteractions().stream()
            .map(
                (e) ->
                    MessageMapper.INSTANCE.toMessage(
                        getActorByIdOutPort.query(e.getActor()).orElseThrow().getName(), e))
            .toList();

    List<Actor> featuredActors = getActorListByIdOutPort.query(session.getFeaturedActors());
    List<Outfit> wornOutfits =
        getOutfitListByIdOutPort.query(
            featuredActors.stream()
                .filter(e -> e.getCurrentOutfit().isPresent())
                .map(e -> e.getCurrentOutfit().get())
                .toList());
    List<GenerateMachineInteractionOutPort.PerformanceDto> performances =
        session.getPerformances().stream()
            .map(e -> MessageMapper.INSTANCE.toDto(e, scenario, featuredActors, wornOutfits))
            .toList();

    User user = getUserByIdOutPort.query(userId).orElseThrow();
    GenerateMachineInteractionOutPort.MachineResponse response =
        generateMachineInteractionOutPort.execute(
            new GenerateMachineInteractionOutPort.Command(
                scenario.getDescription(),
                currentContext,
                actingActor,
                performances,
                previousMessages,
                session.getLocale().getDisplayLanguage(Locale.ENGLISH),
                GenerateMachineInteractionOutPort.TextGenMapper.INSTANCE.toSettingsEntity(
                    user.getSettings()),
                memory.getFragments().stream()
                    .map(e -> GenerateMachineInteractionOutPort.TextGenMapper.INSTANCE.toDto(e))
                    .toList()));
    session.interact(
        response.thoughts().map(e -> new InteractionText(e.text(), e.reasoning())),
        response.action().map(e -> new InteractionText(e.text(), e.reasoning())),
        new InteractionText(response.speech().text(), response.speech().reasoning()),
        cmd.role(),
        Mood.optionalValueOf(response.mood()));

    saveSessionOutPort.save(session);
  }
}
