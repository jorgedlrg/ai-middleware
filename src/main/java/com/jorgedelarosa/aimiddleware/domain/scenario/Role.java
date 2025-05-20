package com.jorgedelarosa.aimiddleware.domain.scenario;

import com.jorgedelarosa.aimiddleware.domain.Actor;
import com.jorgedelarosa.aimiddleware.domain.Entity;
import java.util.UUID;

/**
 * @author jorge
 */
public class Role extends Entity {

  private final Actor actor;

  public Role() {
    super(UUID.randomUUID());
    actor = new Actor();
  }

  public Actor getActor() {
    return actor;
  }
}
