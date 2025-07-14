package com.jorgedelarosa.aimiddleware.domain.session;

import com.jorgedelarosa.aimiddleware.domain.Entity;
import com.jorgedelarosa.aimiddleware.domain.Validator;
import java.time.Instant;
import java.util.Optional;
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
  private final UUID actor;
  private final UUID context;
  private final Optional<Interaction> parent;
  private final Integer level;
  private final Optional<String> mood;
  private final Optional<String> emoji;

  private Interaction(
      String thoughtText,
      String spokenText,
      String actionText,
      Instant timestamp,
      UUID role,
      UUID actor,
      UUID id,
      UUID context,
      Optional<Interaction> parent,
      Integer level,
      Optional<String> mood,
      Optional<String> emoji) {
    super(id);
    this.thoughtText = thoughtText;
    this.spokenText = spokenText;
    this.actionText = actionText;
    this.timestamp = timestamp;
    this.role = role;
    this.actor = actor;
    this.context = context;
    this.parent = parent;
    this.level = level;
    this.mood = mood;
    this.emoji = emoji;
  }

  public static Interaction create(
      String thoughtText,
      String spokenText,
      String actionText,
      UUID role,
      UUID actor,
      UUID context,
      Optional<Interaction> parent,
      Optional<String> mood,
      Optional<String> emoji) {
    Integer level = 0;
    if (parent.isPresent()) {
      level = parent.get().getLevel() + 1;
    }
    Interaction interaction =
        new Interaction(
            thoughtText,
            spokenText,
            actionText,
            Instant.now(),
            role,
            actor,
            UUID.randomUUID(),
            context,
            parent,
            level,
            mood,
            emoji);
    interaction.validate();
    return interaction;
  }

  public static Interaction restore(
      UUID id,
      String thoughtText,
      String spokenText,
      String actionText,
      long timestamp,
      UUID role,
      UUID actor,
      UUID context,
      Optional<Interaction> parent,
      Optional<String> mood,
      Optional<String> emoji) {
    Integer level = 0;
    if (parent.isPresent()) {
      level = parent.get().getLevel() + 1;
    }
    Interaction interaction =
        new Interaction(
            thoughtText,
            spokenText,
            actionText,
            Instant.ofEpochMilli(timestamp),
            role,
            actor,
            id,
            context,
            parent,
            level,
            mood,
            emoji);
    interaction.validate();
    return interaction;
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

  public UUID getActor() {
    return actor;
  }

  public UUID getContext() {
    return context;
  }

  public Optional<Interaction> getParent() {
    return parent;
  }

  public Integer getLevel() {
    return level;
  }

  public Optional<String> getMood() {
    return mood;
  }

  public Optional<String> getEmoji() {
    return emoji;
  }

  @Override
  public boolean validate() {
    if (Validator.strNotEmpty.validate(spokenText)
        && timestamp != null
        && role != null
        && actor != null
        && context != null
        && parent != null
        && level != null
        && mood != null
        && emoji != null) return true;
    else
      throw new RuntimeException(
          String.format("%s %s not valid", this.getClass().getName(), getId()));
  }
}
