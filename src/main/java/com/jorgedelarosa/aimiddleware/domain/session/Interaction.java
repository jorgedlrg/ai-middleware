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

  private final Optional<InteractionText> thoughtText;
  private final InteractionText spokenText;
  private final Optional<InteractionText> actionText;
  private final Instant timestamp;
  private final UUID role;
  private final UUID actor;
  private final UUID context;
  private final Optional<Interaction> parent;
  private final Integer level;
  private final Optional<Mood> mood;

  private Interaction(
      Optional<InteractionText> thoughtText,
      InteractionText spokenText,
      Optional<InteractionText> actionText,
      Instant timestamp,
      UUID role,
      UUID actor,
      UUID id,
      UUID context,
      Optional<Interaction> parent,
      Integer level,
      Optional<Mood> mood) {
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
  }

  public static Interaction create(
      Optional<InteractionText> thoughtText,
      InteractionText spokenText,
      Optional<InteractionText> actionText,
      UUID role,
      UUID actor,
      UUID context,
      Optional<Interaction> parent,
      Optional<Mood> mood) {
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
            mood);
    interaction.validate();
    return interaction;
  }

  public static Interaction restore(
      UUID id,
      Optional<InteractionText> thoughtText,
      InteractionText spokenText,
      Optional<InteractionText> actionText,
      long timestamp,
      UUID role,
      UUID actor,
      UUID context,
      Optional<Interaction> parent,
      Optional<Mood> mood) {
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
            mood);
    interaction.validate();
    return interaction;
  }

  public Optional<InteractionText> getThoughtText() {
    return thoughtText;
  }

  public InteractionText getSpokenText() {
    return spokenText;
  }

  public Optional<InteractionText> getActionText() {
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

  public Optional<Mood> getMood() {
    return mood;
  }

  @Override
  public boolean validate() {
    if (Validator.strNotEmpty.validate(spokenText.getText())
        && actionText != null
        && thoughtText != null
        && timestamp != null
        && role != null
        && actor != null
        && context != null
        && parent != null
        && level != null
        && mood != null) return true;
    else
      throw new RuntimeException(
          String.format("%s %s not valid", this.getClass().getName(), getId()));
  }
}
