package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.adapter.out.message.EventEnvelope;
import com.jorgedelarosa.aimiddleware.adapter.out.message.MessagePublisher;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.OutboxEventEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.OutboxEventRepository;
import com.jorgedelarosa.aimiddleware.application.port.out.PublishDomainEventOutPort;
import com.jorgedelarosa.aimiddleware.domain.DomainEvent;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.json.JsonMapper;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OutboxAdapter implements PublishDomainEventOutPort {
  private final OutboxEventRepository repository;
  private final JsonMapper jsonMapper;
  private final MessagePublisher messagePublisher;

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
    oee.setCreatedAt(Instant.now().toEpochMilli());

    repository.save(oee);
  }

  @Scheduled(fixedDelay = 1000)
  public void processOutboxEvents() {
    List<OutboxEventEntity> unpublished = repository.findByProcessedFalse();

    for (OutboxEventEntity oee : unpublished) {
      try {
        Class clazz = Class.forName(oee.getEventType());
        EventEnvelope envelope = deserialize(oee.getPayload(), clazz);
        messagePublisher.publishMessage(envelope);
        // Mark as processed
        oee.setProcessed(true);
        oee.setProcessedOn(Instant.now().toEpochMilli());
      } catch (Exception ex) {
        String message = ex.getMessage();
        log.warn(message);
        oee.setRetryCount(oee.getRetryCount() + 1);
        oee.setErrorMessage(message);
      }
      repository.save(oee);
    }
  }

  private <T extends DomainEvent> EventEnvelope<T> deserialize(String payload, Class<T> clazz) {
    JavaType javaType =
        jsonMapper.getTypeFactory().constructParametricType(EventEnvelope.class, clazz);
    EventEnvelope<T> envelope;
    try {
      envelope = jsonMapper.readValue(payload, javaType);
    } catch (JacksonException ex) {
      log.error(ex.getMessage());
      throw new RuntimeException(ex);
    }

    return envelope;
  }
}