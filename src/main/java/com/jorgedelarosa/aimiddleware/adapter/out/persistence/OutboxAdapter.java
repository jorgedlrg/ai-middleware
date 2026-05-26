package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.adapter.out.message.EventEnvelope;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.OutboxEventEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.OutboxEventRepository;
import com.jorgedelarosa.aimiddleware.application.port.out.PublishDomainEventOutPort;
import com.jorgedelarosa.aimiddleware.domain.DomainEvent;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxAdapter implements PublishDomainEventOutPort {
  private final OutboxEventRepository repository;
  private final JsonMapper jsonMapper;

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
      oee.setPayload(jsonMapper.writeValueAsString(envelope));
    } catch (JacksonException ex) {
      log.error(ex.getOriginalMessage());
      throw new RuntimeException(ex);
    }

    repository.save(oee);
  }
}
