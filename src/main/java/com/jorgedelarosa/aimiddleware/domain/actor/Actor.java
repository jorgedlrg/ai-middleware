package com.jorgedelarosa.aimiddleware.domain.actor;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

/**
 * @author jorge
 */

//TODO Add validation on all entities
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

  public static Actor create(String name, String physicalDescription, Optional<Mind> mind) {
    return new Actor(
        UUID.randomUUID(), name, physicalDescription, mind, new ArrayList<>(), Optional.empty());
  }

  public static Actor restore(
      UUID id,
      String name,
      String physicalDescription,
      Optional<Mind> mind,
      List<Outfit> outfits,
      Optional<UUID> currentOutfit) {
    return new Actor(id, name, physicalDescription, mind, outfits, currentOutfit);
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
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPhysicalDescription(String physicalDescription) {
    this.physicalDescription = physicalDescription;
  }
}
