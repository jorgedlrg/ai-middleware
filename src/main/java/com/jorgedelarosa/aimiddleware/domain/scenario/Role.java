package com.jorgedelarosa.aimiddleware.domain.scenario;

import com.jorgedelarosa.aimiddleware.domain.Actor;
import com.jorgedelarosa.aimiddleware.domain.Entity;
import java.util.UUID;

/**
 * @author jorge
 */
public class Role extends Entity {

  // TODO: this is pending refinement
  private final Actor actor;

  public static final UUID USER =
      UUID.fromString("7376f89d-4ca7-423b-95f1-e29a8832ec4a"); // FIXME: remove this dirty hack
  public static final UUID MACHINE =
      UUID.fromString("655cfb3d-c740-48d2-ab4f-51e391c4deaf"); // FIXME: remove this dirty hack

  private Role(UUID id) {
    super(id);
    this.actor = new Actor(); // TODO
  }

  public static Role create() {
    return new Role(UUID.randomUUID());
  }

  public static Role restore(UUID id) {
    return new Role(id);
  }

  public Actor getActor() {
    return actor;
  }
}
