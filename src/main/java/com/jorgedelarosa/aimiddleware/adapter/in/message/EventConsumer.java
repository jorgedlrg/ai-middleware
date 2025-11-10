package com.jorgedelarosa.aimiddleware.adapter.in.message;

import com.jorgedelarosa.aimiddleware.adapter.out.message.EventEnvelope;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.RemoveOutfitAllActorsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.AutoreplyUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.DeleteSessionWithActorUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.DeleteSessionWithScenarioUseCase;
import com.jorgedelarosa.aimiddleware.domain.DomainEvent;
import com.jorgedelarosa.aimiddleware.domain.actor.ActorDeletedEvent;
import com.jorgedelarosa.aimiddleware.domain.actor.OutfitDeletedEvent;
import com.jorgedelarosa.aimiddleware.domain.scenario.ScenarioDeletedEvent;
import com.jorgedelarosa.aimiddleware.domain.session.InteractionAddedEvent;
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
  private final AutoreplyUseCase autoreplyUseCase;

  @EventListener
  public void handleMessage(EventEnvelope<? extends DomainEvent> envelope) {
    log.info(String.format("Received %s", envelope.getEvent().getClass().getSimpleName()));
    switch (envelope.getEvent()) {
      case ActorDeletedEvent event -> {
        deleteSessionWithActorUseCase.execute(
            new DeleteSessionWithActorUseCase.Command(event.getAggregateId().getId()));
      }
      case OutfitDeletedEvent event -> {
        removeOutfitAllActorsUseCase.execute(
            new RemoveOutfitAllActorsUseCase.Command(event.getAggregateId().getId()));
      }
      case ScenarioDeletedEvent event -> {
        deleteSessionWithScenarioUseCase.execute(
            new DeleteSessionWithScenarioUseCase.Command(event.getAggregateId().getId()));
      }
      case InteractionAddedEvent event -> {
        autoreplyUseCase.execute(
            new AutoreplyUseCase.Command(event.getAggregateId().getId(), event.getAutoreplyRole()));
      }
      default -> log.warn(String.format("Unhandled event type: %s", envelope.getEventType()));
    }
  }
}
