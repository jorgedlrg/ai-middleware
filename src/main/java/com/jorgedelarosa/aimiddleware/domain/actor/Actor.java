package com.jorgedelarosa.aimiddleware.domain.actor;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import com.jorgedelarosa.aimiddleware.domain.Validator;
import com.jorgedelarosa.aimiddleware.domain.session.Mood;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * @author jorge
 */
public class Actor extends AggregateRoot {

  private String name;
  private String profile;
  private String physicalDescription;
  private Optional<Mind> mind;
  private Optional<UUID> currentOutfit;
  private byte[] portrait;
  private final Map<Mood, List<byte[]>> moodPortraits;

  private Actor(
      UUID id,
      String name,
      String profile,
      String physicalDescription,
      Optional<Mind> mind,
      Optional<UUID> currentOutfit,
      byte[] portrait,
      Map<Mood, List<byte[]>> moodPortraits) {
    super(Actor.class, id);
    this.name = name;
    this.profile = profile;
    this.physicalDescription = physicalDescription;
    this.mind = mind;
    this.currentOutfit = currentOutfit;
    this.portrait = portrait;
    this.moodPortraits = moodPortraits;
  }

  public static Actor create(
      String name, String profile, String physicalDescription, String personality) {
    UUID id = UUID.randomUUID();
    Optional<Mind> mind = Optional.empty();
    if (personality != null && !personality.equals("")) {
      mind = Optional.of(Mind.create(id, personality));
    }
    Actor actor =
        new Actor(
            id,
            name,
            profile,
            physicalDescription,
            mind,
            Optional.empty(),
            new byte[0],
            new HashMap<>());
    actor.validate();
    return actor;
  }

  public static Actor restore(
      UUID id,
      String name,
      String profile,
      String physicalDescription,
      Optional<Mind> mind,
      Optional<UUID> currentOutfit,
      byte[] portrait,
      Map<Mood, List<byte[]>> moodPortraits) {
    Actor actor =
        new Actor(
            id, name, profile, physicalDescription, mind, currentOutfit, portrait, moodPortraits);
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

  public byte[] getPortrait() {
    return portrait;
  }

  public void setPortrait(byte[] portrait) {
    this.portrait = portrait;
    validate();
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

  public byte[] getMoodPortrait(Mood mood) {
    List<byte[]> assets = moodPortraits.get(mood);
    if (assets != null && !assets.isEmpty()) {
      int index = new Random().nextInt(assets.size());
      return assets.get(index);
    } else {
      return portrait;
    }
  }

  @Override
  public boolean validate() {
    if (Validator.strNotEmpty.validate(name)
            && Validator.strNotEmpty.validate(profile)
            && Validator.strNotEmpty.validate(physicalDescription)
            && currentOutfit != null
            && mind.isPresent()
        ? mind.get().validate()
        : true && portrait != null && moodPortraits != null) return true;
    else
      throw new RuntimeException(
          String.format("%s %s not valid", this.getClass().getName(), getId()));
  }
}
