package com.jorgedelarosa.aimiddleware.domain.actor;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import com.jorgedelarosa.aimiddleware.domain.Validator;
import java.util.UUID;

/**
 * @author jorge
 */
public class Outfit extends AggregateRoot {
  private String name;
  private String description;

  private Outfit(UUID id, String name, String description) {
    super(Outfit.class, id);
    this.name = name;
    this.description = description;
  }

  public static Outfit create(String name, String description) {
    return new Outfit(UUID.randomUUID(), name, description);
  }

  public static Outfit restore(UUID id, String name, String description) {
    return new Outfit(id, name, description);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean validate() {
    if (Validator.strNotEmpty.validate(name) && Validator.strNotEmpty.validate(description))
      return true;
    else
      throw new RuntimeException(
          String.format("%s %s not valid", this.getClass().getName(), getId()));
  }
}
