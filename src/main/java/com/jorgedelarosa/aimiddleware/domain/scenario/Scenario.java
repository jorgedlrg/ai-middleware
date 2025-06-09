package com.jorgedelarosa.aimiddleware.domain.scenario;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author jorge
 */
public class Scenario extends AggregateRoot {

  private final List<Context> contexts;
  private final List<Role> roles;

  private Scenario(List<Context> contexts, List<Role> roles, Class clazz, UUID id) {
    super(clazz, id);
    this.contexts = contexts;
    this.roles = roles;
  }

  public static Scenario create() {
    return new Scenario(new ArrayList<>(), new ArrayList<>(), Scenario.class, UUID.randomUUID());
  }

  public static Scenario restore(UUID id, List<Context> contexts, List<Role> roles) {

    return new Scenario(contexts, roles, Scenario.class, id);
  }

  // TODO: Maybe I'll create the Role here later on
  public void addRole(Role role) {
    roles.add(role);
  }

  // TODO: Avoid external modification
  public List<Context> getContexts() {
    return contexts;
  }

  public List<Role> getRoles() {
    return roles;
  }
}
