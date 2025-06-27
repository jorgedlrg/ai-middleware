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

    return new Scenario(name, contexts, roles, Scenario.class, id);
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
