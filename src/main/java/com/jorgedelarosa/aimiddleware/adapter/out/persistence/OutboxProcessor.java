package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorgedelarosa.aimiddleware.adapter.out.message.EventEnvelope;
import com.jorgedelarosa.aimiddleware.adapter.out.message.MessagePublisher;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.OutboxEventEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.OutboxEventRepository;
import com.jorgedelarosa.aimiddleware.domain.DomainEvent;
import java.time.Instant;
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
public class OutboxProcessor {
  private final OutboxEventRepository repository;
  private final ObjectMapper objectMapper;
  private final MessagePublisher messagePublisher;

  @Scheduled(fixedDelay = 1000)
  public void processOutboxEvents() {
    List<OutboxEventEntity> unpublished = repository.findByProcessedFalse();

    for (OutboxEventEntity oee : unpublished) {
      try {
        Class clazz = Class.forName(oee.getEventType());
        EventEnvelope envelope = deserialize(oee.getPayload(), clazz);
        messagePublisher.publishMessage(envelope);
        log.info("Published message");
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
      log.info("outbox updated");
    }
  }

  private <T extends DomainEvent> EventEnvelope<T> deserialize(String payload, Class<T> clazz) {
    JavaType javaType =
        objectMapper.getTypeFactory().constructParametricType(EventEnvelope.class, clazz);
    EventEnvelope<T> envelope;
    try {
      envelope = objectMapper.readValue(payload, javaType);
    } catch (JsonProcessingException ex) {
      log.error(ex.getMessage());
      throw new RuntimeException(ex);
    }

    return envelope;
  }
}
