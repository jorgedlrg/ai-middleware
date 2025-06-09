package com.jorgedelarosa.aimiddleware.domain.scenario;

import com.jorgedelarosa.aimiddleware.domain.Entity;
import java.util.Optional;
import java.util.UUID;

/**
 * @author jorge
 */
public class Role extends Entity {

  private final String name;
  private Optional<UUID> actor;

  private Role(UUID id, Optional<UUID> actor, String name) {
    super(id);
    this.actor = actor;
    this.name = name;
  }

  public static Role create(String name) {
    return new Role(UUID.randomUUID(), Optional.empty(), name);
  }

  public static Role restore(UUID id, String name) {
    return new Role(id, Optional.empty(), name);
  }

  public void perform(UUID actor) {
    this.actor = Optional.of(actor);
  }

  public Optional<UUID> getActor() {
    return actor;
  }

  public String getName() {
    return name;
  }
}
