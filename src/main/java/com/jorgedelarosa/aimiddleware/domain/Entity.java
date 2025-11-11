package com.jorgedelarosa.aimiddleware.domain;

import java.util.UUID;

/**
 * @author jorge
 */
public abstract class Entity {
  protected final UUID entityId;

  protected Entity(UUID id) {
    this.entityId = id;
  }

  public UUID getId() {
    return entityId;
  }

  public abstract boolean validate();
}
