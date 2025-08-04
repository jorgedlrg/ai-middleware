package com.jorgedelarosa.aimiddleware.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author jorge
 * @param <T>
 */
public class EventEnvelope<T extends DomainEvent> {
  private final T event;
  private final Map<String, Object> metadata;
  private final String correlationId;
  private final String causationId; // The ID of the event that caused this. Useful for idempotency.

  public EventEnvelope(
      T event, Map<String, Object> metadata, String correlationId, String causationId) {
    this.event = event;
    this.metadata = metadata;
    this.correlationId = correlationId;
    this.causationId = causationId;
  }

  public static <T extends DomainEvent> EventEnvelope<T> of(T event) {
    return new EventEnvelope<>(
        event, new HashMap<>(), UUID.randomUUID().toString(), event.getEventId().toString());
  }

  public T getEvent() {
    return event;
  }

  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public String getCausationId() {
    return causationId;
  }
}