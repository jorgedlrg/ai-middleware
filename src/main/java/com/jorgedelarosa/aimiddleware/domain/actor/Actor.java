package com.jorgedelarosa.aimiddleware.domain.actor;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import com.jorgedelarosa.aimiddleware.domain.Validator;
import java.util.Optional;
import java.util.UUID;

/**
 * @author jorge
 */
public class Actor extends AggregateRoot {

  private String name;
  private String physicalDescription;
  private Optional<Mind> mind;
  private Optional<UUID> currentOutfit;
  private byte[] portrait;

  private Actor(
      UUID id,
      String name,
      String physicalDescription,
      Optional<Mind> mind,
      Optional<UUID> currentOutfit,
      byte[] portrait) {
    super(Actor.class, id);
    this.name = name;
    this.physicalDescription = physicalDescription;
    this.mind = mind;
    this.currentOutfit = currentOutfit;
    this.portrait = portrait;
  }

  public static Actor create(String name, String physicalDescription, String personality) {
    UUID id = UUID.randomUUID();
    Optional<Mind> mind = Optional.empty();
    if (personality != null && !personality.equals("")) {
      mind = Optional.of(Mind.create(id, personality));
    }
    Actor actor = new Actor(id, name, physicalDescription, mind, Optional.empty(), new byte[0]);
    actor.validate();
    return actor;
  }

  public static Actor restore(
      UUID id,
      String name,
      String physicalDescription,
      Optional<Mind> mind,
      Optional<UUID> currentOutfit,
      byte[] portrait) {
    Actor actor = new Actor(id, name, physicalDescription, mind, currentOutfit, portrait);
    actor.validate();
    return actor;
  }

  public String getName() {
    return name;
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
  public boolean validate() {
    if (Validator.strNotEmpty.validate(name)
            && Validator.strNotEmpty.validate(physicalDescription)
            && currentOutfit != null
            && mind.isPresent()
        ? mind.get().validate()
        : true && portrait != null) return true;
    else
      throw new RuntimeException(
          String.format("%s %s not valid", this.getClass().getName(), getId()));
  }
}
