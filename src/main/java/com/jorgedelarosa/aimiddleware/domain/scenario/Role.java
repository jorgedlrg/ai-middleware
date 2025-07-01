package com.jorgedelarosa.aimiddleware.domain.scenario;

import com.jorgedelarosa.aimiddleware.domain.Entity;
import com.jorgedelarosa.aimiddleware.domain.Validator;
import java.util.UUID;

/**
 * @author jorge
 */
public class Role extends Entity {

  private String name;
  private String details;

  private Role(UUID id, String name, String details) {
    super(id);
    this.name = name;
    this.details = details;
  }

  public static Role create(String name, String details) {
    Role role = new Role(UUID.randomUUID(), name, details);
    role.validate();
    return role;
  }

  public static Role restore(UUID id, String name, String details) {
    Role role = new Role(id, name, details);
    role.validate();
    return role;
  }

  public String getName() {
    return name;
  }

  public String getDetails() {
    return details;
  }

  public void setName(String name) {
    this.name = name;
    validate();
  }

  public void setDetails(String details) {
    this.details = details;
    validate();
  }

  @Override
  public boolean validate() {
    if (Validator.strNotEmpty.validate(name) && Validator.strNotEmpty.validate(details))
      return true;
    else
      throw new RuntimeException(
          String.format("%s %s not valid", this.getClass().getName(), getId()));
  }
}
