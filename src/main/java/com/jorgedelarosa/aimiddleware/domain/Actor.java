package com.jorgedelarosa.aimiddleware.domain;

import java.util.UUID;

/**
 * @author jorge
 */
public class Actor extends AggregateRoot {

  private String name;
  private String physicalDescription;

  private Actor(UUID id, String name, String physicalDescription) {
    super(Actor.class, id);
    this.name = name;
    this.physicalDescription = physicalDescription;
  }

  public static Actor create(String name, String physicalDescription) {
    return new Actor(UUID.randomUUID(), name, physicalDescription);
  }

  public static Actor restore(UUID id, String name, String physicalDescription) {
    return new Actor(id, name, physicalDescription);
  }

  public String getName() {
    return name;
  }

  public String getPhysicalDescription() {
    return physicalDescription;
  }
}
