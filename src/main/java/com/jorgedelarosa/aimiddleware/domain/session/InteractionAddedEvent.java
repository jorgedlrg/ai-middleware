package com.jorgedelarosa.aimiddleware.domain.session;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import com.jorgedelarosa.aimiddleware.domain.DomainEvent;
import java.util.Optional;
import java.util.UUID;

/**
 * @author jorge
 */
public class InteractionAddedEvent extends DomainEvent {

  private final Optional<UUID> autoreplyRole;

  public InteractionAddedEvent(
      AggregateRoot.AggregateId aggregateId,
      Long version,
      Interaction interaction,
      Optional<UUID> autoreplyRole) {
    super(aggregateId, version);
    this.autoreplyRole = autoreplyRole;
  }

  public Optional<UUID> getAutoreplyRole() {
    return autoreplyRole;
  }

  @Override
  public int getSchemaVersion() {
    return 1;
  }
}
