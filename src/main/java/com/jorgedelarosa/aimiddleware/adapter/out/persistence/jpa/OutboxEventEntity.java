package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * @author jorge
 */
@Entity(name = "outbox_event")
@Data
public class OutboxEventEntity {
  @Id private String id;

  @Column(name = "aggregate_id")
  private String aggregateId;

  @Column(name = "event_type")
  private String eventType;

  private String payload;

  @Column(name = "created_at")
  private long createdAt;

  private boolean processed = false;

  @Column(name = "processed_on")
  private long processedOn;

  @Column(name = "retry_count")
  private int retryCount = 0;

  @Column(name = "error_message")
  private String errorMessage;
}
