package com.jorgedelarosa.aimiddleware.domain.session;

import java.util.UUID;

/**
 * @author jorge
 */
public class Performance {
  private final UUID actor;
  private final UUID role;

  public Performance(UUID actor, UUID role) {
    if (actor == null) {
      throw new RuntimeException("Performance actor cannot be null");
    }
    if (role == null) {
      throw new RuntimeException("Performance role cannot be null");
    }
    this.actor = actor;
    this.role = role;
  }

  public UUID getActor() {
    return actor;
  }

  public UUID getRole() {
    return role;
  }
}
