package com.jorgedelarosa.aimiddleware.domain.scenario;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import com.jorgedelarosa.aimiddleware.domain.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author jorge
 */
public class Scenario extends AggregateRoot {

  private String name;
  private final List<Context> contexts;
  private final List<Role> roles;

  private Scenario(String name, List<Context> contexts, List<Role> roles, Class clazz, UUID id) {
    super(clazz, id);
    this.name = name;
    this.contexts = contexts;
    this.roles = roles;
  }

  public static Scenario create(String name) {
    Scenario scenario =
        new Scenario(name, new ArrayList<>(), new ArrayList<>(), Scenario.class, UUID.randomUUID());
    scenario.validate();
    return scenario;
  }

  public static Scenario restore(UUID id, String name, List<Context> contexts, List<Role> roles) {
    Scenario scenario =
        new Scenario(name, new ArrayList(contexts), new ArrayList(roles), Scenario.class, id);
    scenario.validate();
    return scenario;
  }

  public void addNewContext(String name, String physicalDescription) {
    contexts.add(Context.create(name, physicalDescription));
    validate();
  }

  public void modifyContext(UUID contextId, String name, String physicalDescription) {
    contexts.stream()
        .filter(e -> e.getId().equals(contextId))
        .forEach(
            e -> {
              e.setName(name);
              e.setPhysicalDescription(physicalDescription);
            });
    validate();
  }

  public void deleteContext(UUID contextId) {
    for (int i = 0; i < contexts.size(); ++i) {
      if (contexts.get(i).getId().equals(contextId)) {
        contexts.remove(i);
        break;
      }
    }
    validate();
  }

  public void addNewRole(String name, String details) {
    roles.add(Role.create(name, details));
    validate();
  }

  public void modifyRole(UUID roleId, String name, String details) {
    roles.stream()
        .filter(e -> e.getId().equals(roleId))
        .forEach(
            e -> {
              e.setName(name);
              e.setDetails(details);
            });
    validate();
  }

  public void deleteRole(UUID roleId) {
    for (int i = 0; i < roles.size(); ++i) {
      if (roles.get(i).getId().equals(roleId)) {
        roles.remove(i);
        break;
      }
    }
    validate();
  }

  public String getName() {
    return name;
  }

  public List<Context> getContexts() {
    return List.copyOf(contexts);
  }

  public List<Role> getRoles() {
    return List.copyOf(roles);
  }

  public void setName(String name) {
    this.name = name;
    validate();
  }

  @Override
  public boolean validate() {
    if (Validator.strNotEmpty.validate(name)) return true;
    else
      throw new RuntimeException(
          String.format("%s %s not valid", this.getClass().getName(), getId()));
  }
}
