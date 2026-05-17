package com.jorgedelarosa.aimiddleware.domain.actor;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import java.util.Optional;
import java.util.UUID;

public class Actor extends AggregateRoot {

  private String name;
  private String profile;
  private String physicalDescription;
  private Optional<Mind> mind;
  private Optional<UUID> currentOutfit;

  private Actor(
      UUID id,
      String name,
      String profile,
      String physicalDescription,
      Optional<Mind> mind,
      Optional<UUID> currentOutfit) {
    super(Actor.class, id);
    this.name = name;
    this.profile = profile;
    this.physicalDescription = physicalDescription;
    this.mind = mind;
    this.currentOutfit = currentOutfit;
  }

  public static Actor create(
      String name, String profile, String physicalDescription, String personality) {
    UUID id = UUID.randomUUID();
    Optional<Mind> mind = Optional.empty();
    if (personality != null && !personality.equals("")) {
      mind = Optional.of(Mind.create(id, personality));
    }
    Actor actor = new Actor(id, name, profile, physicalDescription, mind, Optional.empty());
    actor.validate();
    return actor;
  }

  public static Actor restore(
      UUID id,
      String name,
      String profile,
      String physicalDescription,
      Optional<Mind> mind,
      Optional<UUID> currentOutfit) {
    Actor actor = new Actor(id, name, profile, physicalDescription, mind, currentOutfit);
    actor.validate();
    return actor;
  }

  public String getName() {
    return name;
  }

  public String getProfile() {
    return profile;
  }

  public String getPhysicalDescription() {
    return physicalDescription;
  }

  public Optional<Mind> getMind() {
    return mind;
  }

  public Optional<UUID> getCurrentOutfit() {
    return currentOutfit;
  }

  public void chooseOutfit(UUID newOutfit) {
    this.currentOutfit = Optional.ofNullable(newOutfit);
    validate();
  }

  public void setName(String name) {
    this.name = name;
    validate();
  }

  public void setProfile(String profile) {
    this.profile = profile;
    validate();
  }

  public void setPhysicalDescription(String physicalDescription) {
    this.physicalDescription = physicalDescription;
    validate();
  }

  public void setPersonality(String personality) {
    if (personality != null && !personality.equals("")) {
      if (mind.isPresent()) {
        mind.get().setPersonality(personality);
      } else {
        mind = Optional.of(Mind.create(entityId, personality));
      }
    } else {
      mind = Optional.empty();
    }
    validate();
  }

  @Override
  public boolean isValid() {
    return (name != null && !name.isBlank())
        && (profile != null && !profile.isBlank())
        && (physicalDescription != null && !physicalDescription.isBlank())
        && currentOutfit != null
        && (mind.isEmpty() || mind.get().isValid());
  }
}