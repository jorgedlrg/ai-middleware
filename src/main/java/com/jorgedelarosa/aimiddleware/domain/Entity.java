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

  public void validate() {
    if (!isValid()) {
      throw new RuntimeException(
          String.format("%s %s not valid", getClass().getName(), getId()));
    }
  }

  public boolean isValid() {
    return entityId != null;
  }
}