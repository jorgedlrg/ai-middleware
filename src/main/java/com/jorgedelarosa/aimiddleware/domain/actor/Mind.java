package com.jorgedelarosa.aimiddleware.domain.actor;

import com.jorgedelarosa.aimiddleware.domain.Entity;
import java.util.UUID;

/**
 * This is a weak entity that extends Actor. Aggregation is more flexible than inheritance and more
 * maintainable.
 *
 * @author jorge
 */
public class Mind extends Entity {

  private String personality;

  private Mind(String personality, UUID actor) {
    super(actor);
    this.personality = personality;
  }

  public static Mind create(UUID actor, String personality) {
    return new Mind(personality, actor);
  }

  public static Mind restore(UUID actor, String personality) {
    return new Mind(personality, actor);
  }

  public String getPersonality() {
    return personality;
  }
}
