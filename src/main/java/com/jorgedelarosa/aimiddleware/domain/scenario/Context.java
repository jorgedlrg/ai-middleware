package com.jorgedelarosa.aimiddleware.domain.scenario;

import com.jorgedelarosa.aimiddleware.domain.Entity;
import java.util.UUID;

/**
 * @author jorge
 */
public class Context extends Entity {

  private String name;
  private String physicalDescription;

  private Context(UUID id, String name, String physicalDescription) {
    super(id);
    this.name = name;
    this.physicalDescription = physicalDescription;
  }

  public static Context create(String name, String physicalDescription) {
    return new Context(UUID.randomUUID(), name, physicalDescription);
  }

  public static Context restore(UUID id, String name, String physicalDescription) {
    return new Context(id, name, physicalDescription);
  }

  public String getName() {
    return name;
  }

  public String getPhysicalDescription() {
    return physicalDescription;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPhysicalDescription(String physicalDescription) {
    this.physicalDescription = physicalDescription;
  }
}
