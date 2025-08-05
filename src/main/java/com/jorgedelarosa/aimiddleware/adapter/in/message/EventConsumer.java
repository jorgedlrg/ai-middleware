package com.jorgedelarosa.aimiddleware.adapter.in.message;

import com.jorgedelarosa.aimiddleware.adapter.out.message.EventEnvelope;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.RemoveOutfitAllActorsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.DeleteSessionWithActorUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.DeleteSessionWithScenarioUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EventConsumer {

  private final DeleteSessionWithActorUseCase deleteSessionWithActorUseCase;
  private final DeleteSessionWithScenarioUseCase deleteSessionWithScenarioUseCase;
  private final RemoveOutfitAllActorsUseCase removeOutfitAllActorsUseCase;

  @EventListener
  public void handleMessage(EventEnvelope envelope) {
    switch (envelope.getEventType()) {
      case "com.jorgedelarosa.aimiddleware.domain.actor.ActorDeletedEvent" -> {
        log.info("Handling com.jorgedelarosa.aimiddleware.domain.actor.ActorDeletedEvent");
        deleteSessionWithActorUseCase.execute(
            new DeleteSessionWithActorUseCase.Command(
                envelope.getEvent().getAggregateId().getId()));
      }
      case "com.jorgedelarosa.aimiddleware.domain.actor.OutfitDeletedEvent" -> {
        log.info("Handling com.jorgedelarosa.aimiddleware.domain.actor.OutfitDeletedEvent");
        removeOutfitAllActorsUseCase.execute(
            new RemoveOutfitAllActorsUseCase.Command(envelope.getEvent().getAggregateId().getId()));
      }
      case "com.jorgedelarosa.aimiddleware.domain.scenario.ScenarioDeletedEvent" -> {
        log.info("Handling com.jorgedelarosa.aimiddleware.domain.scenario.ScenarioDeletedEvent");
        deleteSessionWithScenarioUseCase.execute(
            new DeleteSessionWithScenarioUseCase.Command(
                envelope.getEvent().getAggregateId().getId()));
      }
      default -> log.info(String.format("Unhandled event type: %s", envelope.getEventType()));
    }
  }
}
