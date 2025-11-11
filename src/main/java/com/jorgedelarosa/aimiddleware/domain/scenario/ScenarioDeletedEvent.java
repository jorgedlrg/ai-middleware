package com.jorgedelarosa.aimiddleware.domain.scenario;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import com.jorgedelarosa.aimiddleware.domain.DomainEvent;

/**
 * @author jorge
 */
public class ScenarioDeletedEvent extends DomainEvent {

  public ScenarioDeletedEvent(AggregateRoot.AggregateId aggregateId, Long version) {
    super(aggregateId, version);
  }

  @Override
  public int getSchemaVersion() {
    return 1;
  }
}
