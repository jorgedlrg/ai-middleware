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

  public Scenario() {
    super(Scenario.class, UUID.randomUUID());
    contexts = new ArrayList<>();
    roles = new ArrayList<>();
  }

  public List<Context> getContexts() {
    return contexts;
  }

  public List<Role> getRoles() {
    return roles;
  }
}
