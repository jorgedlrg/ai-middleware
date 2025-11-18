package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorgedelarosa.aimiddleware.adapter.out.message.EventEnvelope;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.OutboxEventEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.OutboxEventRepository;
import com.jorgedelarosa.aimiddleware.application.port.out.PublishDomainEventOutPort;
import com.jorgedelarosa.aimiddleware.domain.DomainEvent;
import java.time.Instant;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxAdapter implements PublishDomainEventOutPort {
  private final OutboxEventRepository repository;
  private final ObjectMapper objectMapper;

  @Override
  public void publishDomainEvent(DomainEvent event) {
    String correlationId = "TODO"; // TODO: these two should come from the context
    String causationId = "TODO";
    EventEnvelope envelope =
        new EventEnvelope(
            event, event.getClass().getName(), new HashMap<>(), correlationId, causationId);

    OutboxEventEntity oee = new OutboxEventEntity();
    oee.setId(envelope.getEvent().getEventId().toString());
    oee.setAggregateId(envelope.getEvent().getAggregateId().toString());
    oee.setEventType(envelope.getEventType());
    try {
      oee.setPayload(objectMapper.writeValueAsString(envelope));
    } catch (JsonProcessingException ex) {
      log.error(ex.getOriginalMessage());
      throw new RuntimeException(ex);
    }
    oee.setCreatedAt(Instant.now().toEpochMilli());

    repository.save(oee);
  }
}