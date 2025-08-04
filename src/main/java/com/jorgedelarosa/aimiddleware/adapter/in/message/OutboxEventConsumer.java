package com.jorgedelarosa.aimiddleware.adapter.in.message;

import com.jorgedelarosa.aimiddleware.adapter.out.message.EventEnvelope;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.RemoveOutfitAllActorsUseCase;
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
public class OutboxEventConsumer {

  private final RemoveOutfitAllActorsUseCase removeOutfitAllActorsUseCase;

  @EventListener
  public void handleMessage(EventEnvelope envelope) {
    switch (envelope.getEventType()) {
      case "com.jorgedelarosa.aimiddleware.application.port.in.actor.DeleteOutfitUseCase$OutfitDeletedEvent" -> {
        log.info("removing outfit from actors..");

        removeOutfitAllActorsUseCase.execute(
            new RemoveOutfitAllActorsUseCase.Command(envelope.getEvent().getAggregateId().getId()));
      }
      default -> log.info(String.format("Unhandled event type: %s", envelope.getEventType()));
    }
  }
}
