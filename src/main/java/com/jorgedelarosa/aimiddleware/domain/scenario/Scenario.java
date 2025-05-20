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

  // TODO map with context - role

  public Scenario(Role user, Role machine) {
    super(Scenario.class, UUID.randomUUID());
    contexts = new ArrayList<>();
    roles = new ArrayList<>();
    roles.add(user);
    roles.add(machine);
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
