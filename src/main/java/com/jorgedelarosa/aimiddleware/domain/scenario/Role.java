package com.jorgedelarosa.aimiddleware.domain.scenario;

import com.jorgedelarosa.aimiddleware.domain.Entity;
import java.util.UUID;

/**
 * @author jorge
 */
public class Role extends Entity {

  private String name;
  private String details;

  private Role(UUID id, String name, String details) {
    super(id);
    this.name = name;
    this.details = details;
  }

  public static Role create(String name, String details) {
    return new Role(UUID.randomUUID(), name, details);
  }

  public static Role restore(UUID id, String name, String details) {
    return new Role(id, name, details);
  }

  public String getName() {
    return name;
  }

  public String getDetails() {
    return details;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDetails(String details) {
    this.details = details;
  }
}
