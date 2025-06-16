package com.jorgedelarosa.aimiddleware.domain.actor;

import com.jorgedelarosa.aimiddleware.domain.Entity;
import java.util.UUID;

/**
 * @author jorge
 */
public class Outfit extends Entity {
  private String description;

  private Outfit(String description, UUID id) {
    super(id);
    this.description = description;
  }

  public static Outfit create(String description) {
    return new Outfit(description, UUID.randomUUID());
  }

  public static Outfit restore(UUID id, String description) {
    return new Outfit(description, id);
  }

  public String getDescription() {
    return description;
  }
}
