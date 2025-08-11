package com.jorgedelarosa.aimiddleware.application.port.in.session;

import com.jorgedelarosa.aimiddleware.application.port.out.GenerateMachineInteractionOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorListByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetOutfitListByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetUserByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.actor.Outfit;
import com.jorgedelarosa.aimiddleware.domain.scenario.Context;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.Interaction;
import com.jorgedelarosa.aimiddleware.domain.session.InteractionText;
import com.jorgedelarosa.aimiddleware.domain.session.Mood;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import com.jorgedelarosa.aimiddleware.domain.user.User;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
@Transactional
@Slf4j
public class NextInteractionUseCaseImpl implements NextInteractionUseCase {
  private final UUID userId =
      UUID.fromString(
          "857fa610-b987-454c-96c3-bbf5354f13a0"); // FIXME, get this from security context.. when I
  // implement it

  private final GetSessionByIdOutPort getSessionByIdOutPort;
  private final SaveSessionOutPort saveSessionOutPort;
  private final GetScenarioByIdOutPort getScenarioByIdOutPort;
  private final GetActorByIdOutPort getActorByIdOutPort;
  private final GetActorListByIdOutPort getActorListByIdOutPort;
  private final GetOutfitListByIdOutPort getOutfitListByIdOutPort;
  private final GenerateMachineInteractionOutPort generateMachineInteractionOutPort;
  private final GetUserByIdOutPort getUserByIdOutPort;

  @Override
  public void execute(Command cmd) {
    Session session = getSessionByIdOutPort.query(cmd.session()).orElseThrow();

    try {
      session.setLastInteraction(session.getNextInteraction());
    } catch (NoSuchElementException ex) {
      log.debug(
          String.format(
              "No next interaction for interaction %s. Generating a new one.",
              session.getLastInteraction().getId()));
      Scenario scenario = getScenarioByIdOutPort.query(session.getScenario()).orElseThrow();
      Context currentContext =
          scenario.getContexts().stream()
              .filter(e -> e.getId().equals(session.getCurrentContext()))
              .findFirst()
              .orElseThrow();

      Actor actingActor =
          getActorByIdOutPort
              .query(session.getFeaturedActor(session.getLastInteraction().getRole()).get())
              .orElseThrow();

      List<Interaction> previousInteractions = session.getCurrentInteractions();
      // Removes the last message, since we're regenerating it
      previousInteractions = previousInteractions.subList(0, previousInteractions.size() - 1);
      List<GenerateMachineInteractionOutPort.PreviousMessage> previousMessages =
          previousInteractions.stream()
              .map(
                  (e) ->
                      MachineInteractUseCaseImpl.MessageMapper.INSTANCE.toMessage(
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
              .map(
                  e ->
                      MachineInteractUseCaseImpl.MessageMapper.INSTANCE.toDto(
                          e, scenario, featuredActors, wornOutfits))
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
                      user.getSettings())));
      session.interactNext(
          new InteractionText(response.thoughts().text(), response.thoughts().reasoning()),
          new InteractionText(response.action().text(), response.action().reasoning()),
          new InteractionText(response.speech().text(), response.speech().reasoning()),
          session.getLastInteraction().getRole(),
          Mood.optionalValueOf(response.mood()));
    }
    saveSessionOutPort.save(session);
  }
}
