package com.jorgedelarosa.aimiddleware.adapter.out.message;

import com.jorgedelarosa.aimiddleware.domain.DomainEvent;
import java.util.Map;

/**
 * @author jorge
 * @param <T>
 */
public class EventEnvelope<T extends DomainEvent> {
  private final T event;
  private final String eventType;
  private final Map<String, Object> metadata;
  private final String correlationId;
  private final String causationId; // The ID of the event that caused this. Useful for idempotency.

  public EventEnvelope(
      T event,
      String eventType,
      Map<String, Object> metadata,
      String correlationId,
      String causationId) {
    this.event = event;
    this.eventType = eventType;
    this.metadata = metadata;
    this.correlationId = correlationId;
    this.causationId = causationId;
  }

  public T getEvent() {
    return event;
  }

  public String getEventType() {
    return eventType;
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