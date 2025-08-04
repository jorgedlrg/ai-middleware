package com.jorgedelarosa.aimiddleware.adapter.in.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorgedelarosa.aimiddleware.adapter.out.OutboxEvent;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.OutboxEventEntity;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.RemoveOutfitAllActorsUseCase;
import java.util.StringTokenizer;
import java.util.UUID;
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

  private final ObjectMapper objectMapper;
  private final RemoveOutfitAllActorsUseCase removeOutfitAllActorsUseCase;

  @EventListener
  public void handleMessage(OutboxEvent event) {
    log.info(event.toString());
    OutboxEventEntity oee = (OutboxEventEntity) event.getSource();
    log.info(oee.getEventType());
    StringTokenizer st = new StringTokenizer(oee.getAggregateId(), ":");
    st.nextElement();
    String aggregateClass = st.nextToken();
    UUID id = UUID.fromString(st.nextToken());
    switch (oee.getEventType()) {
      case "com.jorgedelarosa.aimiddleware.application.port.in.actor.DeleteOutfitUseCase$OutfitDeletedEvent" -> {
        log.info("removing outfits..");
        removeOutfitAllActorsUseCase.execute(new RemoveOutfitAllActorsUseCase.Command(id));
      }
      default -> log.info(oee.getEventType());
    }
  }
}
