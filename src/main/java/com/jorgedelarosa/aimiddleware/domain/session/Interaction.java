package com.jorgedelarosa.aimiddleware.domain.session;

import com.jorgedelarosa.aimiddleware.domain.Entity;
import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import java.time.Instant;
import java.util.UUID;

/**
 * @author jorge
 */
public class Interaction extends Entity {

  private final String thoughtText;
  private final String spokenText;
  private final String actionText;
  private final Instant timestamp;
  private final Role role;

  public Interaction(String thoughtText, String spokenText, String actionText, Role role) {
    super(UUID.randomUUID());
    this.thoughtText = thoughtText;
    this.spokenText = spokenText;
    this.actionText = actionText;
    this.role = role;
    this.timestamp = Instant.now();
  }

  public String getThoughtText() {
    return thoughtText;
  }

  public String getSpokenText() {
    return spokenText;
  }

  public String getActionText() {
    return actionText;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public Role getRole() {
    return role;
  }
}
