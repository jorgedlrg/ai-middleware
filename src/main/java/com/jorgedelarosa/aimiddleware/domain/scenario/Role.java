package com.jorgedelarosa.aimiddleware.domain.scenario;

import com.jorgedelarosa.aimiddleware.domain.Entity;
import java.util.UUID;

/**
 * @author jorge
 */
public class Role extends Entity {

  private final UUID actor;

  public static final UUID USER =
      UUID.fromString("7376f89d-4ca7-423b-95f1-e29a8832ec4a"); // FIXME: remove this dirty hack
  public static final UUID MACHINE =
      UUID.fromString("655cfb3d-c740-48d2-ab4f-51e391c4deaf"); // FIXME: remove this dirty hack

  private Role(UUID id, UUID actor) {
    super(id);
    this.actor =  actor;
  }

  public static Role create(UUID actor) {
    return new Role(UUID.randomUUID(),actor);
  }

  public static Role restore(UUID id,UUID actor) {
    return new Role(id,actor);
  }

  public UUID getActor() {
    return actor;
  }
}
