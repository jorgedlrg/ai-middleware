package com.jorgedelarosa.aimiddleware.domain.scenario;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
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
    return new Scenario(
        name, new ArrayList<>(), new ArrayList<>(), Scenario.class, UUID.randomUUID());
  }

  public static Scenario restore(UUID id, String name, List<Context> contexts, List<Role> roles) {

    return new Scenario(name, new ArrayList(contexts), new ArrayList(roles), Scenario.class, id);
  }

  public void addNewContext(String name, String physicalDescription) {
    contexts.add(Context.create(name, physicalDescription));
  }

  public void modifyContext(UUID contextId, String name, String physicalDescription) {
    contexts.stream()
        .filter(e -> e.getId().equals(contextId))
        .forEach(
            e -> {
              e.setName(name);
              e.setPhysicalDescription(physicalDescription);
            });
  }

  public void deleteContext(UUID contextId) {
    for (int i = 0; i < contexts.size(); ++i) {
      if (contexts.get(i).getId().equals(contextId)) {
        contexts.remove(i);
        break;
      }
    }
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
  }
}
