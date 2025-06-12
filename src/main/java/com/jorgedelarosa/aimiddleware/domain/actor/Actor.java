package com.jorgedelarosa.aimiddleware.domain.actor;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import java.util.Optional;
import java.util.UUID;

/**
 * @author jorge
 */
public class Actor extends AggregateRoot {

  private String name;
  private String physicalDescription;
  private Optional<Mind> mind;

  private Actor(UUID id, String name, String physicalDescription, Optional<Mind> mind) {
    super(Actor.class, id);
    this.name = name;
    this.physicalDescription = physicalDescription;
    this.mind = mind;
  }

  public static Actor create(String name, String physicalDescription, Optional<Mind> mind) {
    return new Actor(UUID.randomUUID(), name, physicalDescription, mind);
  }

  public static Actor restore(
      UUID id, String name, String physicalDescription, Optional<Mind> mind) {
    return new Actor(id, name, physicalDescription, mind);
  }

  public String getName() {
    return name;
  }

  public String getPhysicalDescription() {
    return physicalDescription;
  }

  public Optional<Mind> getMind() {
    return mind;
  }
}
