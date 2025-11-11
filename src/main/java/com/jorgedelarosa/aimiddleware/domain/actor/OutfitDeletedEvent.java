package com.jorgedelarosa.aimiddleware.domain.actor;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import com.jorgedelarosa.aimiddleware.domain.DomainEvent;

/**
 * @author jorge
 */
public class OutfitDeletedEvent extends DomainEvent {

  public OutfitDeletedEvent(AggregateRoot.AggregateId aggregateId, Long version) {
    super(aggregateId, version);
  }

  @Override
  public int getSchemaVersion() {
    return 1;
  }
}
