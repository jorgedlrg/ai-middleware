package com.jorgedelarosa.aimiddleware.domain.session;

import com.jorgedelarosa.aimiddleware.domain.Entity;
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
  private final UUID role;

  private Interaction(
      String thoughtText,
      String spokenText,
      String actionText,
      Instant timestamp,
      UUID role,
      UUID id) {
    super(id);
    this.thoughtText = thoughtText;
    this.spokenText = spokenText;
    this.actionText = actionText;
    this.timestamp = timestamp;
    this.role = role;
  }

  public Interaction(String thoughtText, String spokenText, String actionText, UUID role) {
    super(UUID.randomUUID());
    this.thoughtText = thoughtText;
    this.spokenText = spokenText;
    this.actionText = actionText;
    this.role = role;
    this.timestamp = Instant.now();
  }

  public static Interaction restore(
      UUID id,
      String thoughtText,
      String spokenText,
      String actionText,
      long timestamp,
      UUID role) {
    return new Interaction(
        thoughtText, spokenText, actionText, Instant.ofEpochMilli(timestamp), role, id);
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

  public UUID getRole() {
    return role;
  }
}
