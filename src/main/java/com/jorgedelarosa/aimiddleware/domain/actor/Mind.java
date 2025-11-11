package com.jorgedelarosa.aimiddleware.domain.actor;

import com.jorgedelarosa.aimiddleware.domain.Entity;
import com.jorgedelarosa.aimiddleware.domain.Validator;
import java.util.UUID;

/**
 * This is a weak entity that extends Actor. Aggregation is more flexible than inheritance and more
 * maintainable.
 *
 * @author jorge
 */
public class Mind extends Entity {

  private String personality;

  private Mind(String personality, UUID actor) {
    super(actor);
    this.personality = personality;
  }

  public static Mind create(UUID actor, String personality) {
    return new Mind(personality, actor);
  }

  public static Mind restore(UUID actor, String personality) {
    return new Mind(personality, actor);
  }

  public String getPersonality() {
    return personality;
  }

  public void setPersonality(String personality) {
    this.personality = personality;
  }

  @Override
  public boolean validate() {
    if (Validator.strNotEmpty.validate(personality)) return true;
    else
      throw new RuntimeException(
          String.format("%s %s not valid", this.getClass().getName(), getId()));
  }
}
