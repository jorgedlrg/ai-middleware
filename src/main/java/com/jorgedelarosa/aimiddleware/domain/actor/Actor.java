package com.jorgedelarosa.aimiddleware.domain.actor;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import com.jorgedelarosa.aimiddleware.domain.TwoArgsValidator;
import com.jorgedelarosa.aimiddleware.domain.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

/**
 * @author jorge
 */
public class Actor extends AggregateRoot {

  private String name;
  private String physicalDescription;
  private Optional<Mind> mind;
  private List<Outfit> outfits;
  private Optional<UUID> currentOutfit;

  private Actor(
      UUID id,
      String name,
      String physicalDescription,
      Optional<Mind> mind,
      List<Outfit> outfits,
      Optional<UUID> currentOutfit) {
    super(Actor.class, id);
    this.name = name;
    this.physicalDescription = physicalDescription;
    this.mind = mind;
    this.outfits = outfits;
    this.currentOutfit = currentOutfit;
  }

  public static Actor create(String name, String physicalDescription, String personality) {
    UUID id = UUID.randomUUID();
    Optional<Mind> mind = Optional.empty();
    if (personality != null && !personality.equals("")) {
      mind = Optional.of(Mind.create(id, personality));
    }
    Actor actor =
        new Actor(id, name, physicalDescription, mind, new ArrayList<>(), Optional.empty());
    actor.validate();
    return actor;
  }

  public static Actor restore(
      UUID id,
      String name,
      String physicalDescription,
      Optional<Mind> mind,
      List<Outfit> outfits,
      Optional<UUID> currentOutfit) {
    Actor actor =
        new Actor(id, name, physicalDescription, mind, new ArrayList(outfits), currentOutfit);
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

  public Optional<Outfit> getCurrentOutfit() {
    return outfits.stream().filter((e) -> e.getId().equals(currentOutfit.get())).findFirst();
  }

  public void chooseOutfit(UUID currentOutfit) {
    if (outfits.stream().filter((e) -> e.getId().equals(currentOutfit)).findFirst().isPresent()) {
      this.currentOutfit = Optional.of(currentOutfit);
    } else {
      throw new NoSuchElementException(
          String.format(
              "Outfit %s isn't present in the outfit list. Current outfit list size is %s",
              currentOutfit, outfits.size()));
    }
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
            && currentOutfit.isPresent()
        ? TwoArgsValidator.existsIn.validate(currentOutfit.get(), outfits)
        : true && mind.isPresent() ? mind.get().validate() : true) return true;
    else
      throw new RuntimeException(
          String.format("%s %s not valid", this.getClass().getName(), getId()));
  }
}
