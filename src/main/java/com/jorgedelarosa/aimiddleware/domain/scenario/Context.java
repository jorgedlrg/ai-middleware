package com.jorgedelarosa.aimiddleware.domain.scenario;

import com.jorgedelarosa.aimiddleware.domain.Entity;
import com.jorgedelarosa.aimiddleware.domain.Validator;
import java.util.UUID;

/**
 * @author jorge
 */
public class Context extends Entity {

  private String name;
  private String physicalDescription;

  private Context(UUID id, String name, String physicalDescription) {
    super(id);
    this.name = name;
    this.physicalDescription = physicalDescription;
  }

  public static Context create(String name, String physicalDescription) {
    Context context = new Context(UUID.randomUUID(), name, physicalDescription);
    context.validate();
    return context;
  }

  public static Context restore(UUID id, String name, String physicalDescription) {
    Context context = new Context(id, name, physicalDescription);
    context.validate();
    return context;
  }

  public String getName() {
    return name;
  }

  public String getPhysicalDescription() {
    return physicalDescription;
  }

  public void setName(String name) {
    this.name = name;
    validate();
  }

  public void setPhysicalDescription(String physicalDescription) {
    this.physicalDescription = physicalDescription;
    validate();
  }

  @Override
  public boolean validate() {
    if (Validator.strNotEmpty.validate(name) && Validator.strNotEmpty.validate(physicalDescription))
      return true;
    else
      throw new RuntimeException(
          String.format("%s %s not valid", this.getClass().getName(), getId()));
  }
}
