package com.jorgedelarosa.aimiddleware.domain;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot.AggregateId;
import java.time.Instant;
import java.util.UUID;

/**
 * @author jorge
 */
public abstract class DomainEvent {
  private final UUID eventId;
  private final Instant occurredAt;
  private final AggregateId aggregateId;
  private final Long version; // TODO: Aggregate versioning, for event sourcing. maybe in the future

  protected DomainEvent(AggregateId aggregateId, Long version) {
    this.eventId = UUID.randomUUID();
    this.occurredAt = Instant.now();
    this.aggregateId = aggregateId;
    this.version = version;
  }

  public abstract int getSchemaVersion();

  public UUID getEventId() {
    return eventId;
  }

  public Instant getOccurredAt() {
    return occurredAt;
  }

  public AggregateId getAggregateId() {
    return aggregateId;
  }

  public Long getVersion() {
    return version;
  }
}
