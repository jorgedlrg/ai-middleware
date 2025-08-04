package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorgedelarosa.aimiddleware.adapter.out.OutboxEvent;
import com.jorgedelarosa.aimiddleware.adapter.out.message.MessagePublisher;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.OutboxEventEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.OutboxEventRepository;
import com.jorgedelarosa.aimiddleware.application.port.out.PublishDomainEventOutPort;
import com.jorgedelarosa.aimiddleware.domain.DomainEvent;
import com.jorgedelarosa.aimiddleware.domain.EventEnvelope;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OutboxEventPublisher implements PublishDomainEventOutPort {
  private final OutboxEventRepository repository;
  private final ObjectMapper objectMapper;
  private final MessagePublisher messagePublisher;

  @Override
  public void publishDomainEvent(DomainEvent event) {
    String correlationId = "TODO";
    String causationId = "TODO";
    EventEnvelope envelope = new EventEnvelope(event, new HashMap<>(), correlationId, causationId);

    OutboxEventEntity outboxEvent = new OutboxEventEntity();
    outboxEvent.setId(envelope.getEvent().getEventId().toString());
    outboxEvent.setAggregateId(envelope.getEvent().getAggregateId().toString());
    outboxEvent.setEventType(envelope.getEvent().getClass().getName());
    try {
      outboxEvent.setPayload(objectMapper.writeValueAsString(envelope));
    } catch (JsonProcessingException ex) {
      log.error(ex.getOriginalMessage());
      throw new RuntimeException(ex);
    }
    outboxEvent.setCreatedAt(Instant.now().toEpochMilli());

    repository.save(outboxEvent);
  }

  @Scheduled(fixedDelay = 1000)
  public void processOutboxEvents() {
    List<OutboxEventEntity> unpublished = repository.findByProcessedFalse();

    for (OutboxEventEntity oee : unpublished) {
      // Publish to message broker
      OutboxEvent outboxEvent = new OutboxEvent(oee, oee.getPayload());

      messagePublisher.publishMessage(outboxEvent);

      // Mark as processed
      oee.setProcessed(true);
      oee.setProcessedOn(Instant.now().toEpochMilli());
      repository.save(oee);
    }
  }
}