package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "outbox_event")
@Data
@EqualsAndHashCode(callSuper = false)
public class OutboxEventEntity extends BaseJpaEntity {
  @Id private String id;

  @Column(name = "aggregate_id")
  private String aggregateId;

  @Column(name = "event_type")
  private String eventType;

  private String payload;

  private boolean processed = false;

  @Column(name = "processed_on")
  private long processedOn;

  @Column(name = "retry_count")
  private int retryCount = 0;

  @Column(name = "error_message")
  private String errorMessage;
}
