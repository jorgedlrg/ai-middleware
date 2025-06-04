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
  private final boolean
      user; // FIXME this shouldn't be a hacky flag. this should be coming from the role, probably.
  private final UUID context;

  private Interaction(
      String thoughtText,
      String spokenText,
      String actionText,
      Instant timestamp,
      UUID role,
      UUID id,
      boolean user,
      UUID context) {
    super(id);
    this.thoughtText = thoughtText;
    this.spokenText = spokenText;
    this.actionText = actionText;
    this.timestamp = timestamp;
    this.role = role;
    this.user = user;
    this.context = context;
  }

  public static Interaction create(
      String thoughtText,
      String spokenText,
      String actionText,
      UUID role,
      boolean user,
      UUID context) {
    return new Interaction(
        thoughtText, spokenText, actionText, Instant.now(), role, UUID.randomUUID(), user, context);
  }

  public static Interaction restore(
      UUID id,
      String thoughtText,
      String spokenText,
      String actionText,
      long timestamp,
      UUID role,
      boolean user,
      UUID context) {
    return new Interaction(
        thoughtText,
        spokenText,
        actionText,
        Instant.ofEpochMilli(timestamp),
        role,
        id,
        user,
        context);
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

  public boolean isUser() {
    return user;
  }

  public UUID getContext() {
    return context;
  }
}
