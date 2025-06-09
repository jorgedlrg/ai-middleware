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

  private Scenario(List<Context> contexts, List<Role> roles, Class clazz, UUID id) {
    super(clazz, id);
    this.contexts = contexts;
    this.roles = roles;
  }

  public static Scenario create() {
    List<Role> roles = new ArrayList();
    roles.add(
        Role.restore(
            Role.USER,
            UUID.fromString(
                "857fa610-b987-454c-96c3-bbf5354f13a0"))); // TODO: store and load roles properly
    roles.add(
        Role.restore(
            Role.MACHINE,
            UUID.fromString(
                "caa30e65-1886-4366-bfb7-f415af9f4a40"))); // TODO: store and load roles properly
    return new Scenario(new ArrayList<>(), roles, Scenario.class, UUID.randomUUID());
  }

  public static Scenario restore(UUID id, List<Context> contexts) {
    List<Role> roles = new ArrayList();
    roles.add(
        Role.restore(
            Role.USER,
            UUID.fromString(
                "857fa610-b987-454c-96c3-bbf5354f13a0"))); // TODO: store and load roles properly
    roles.add(
        Role.restore(
            Role.MACHINE,
            UUID.fromString(
                "caa30e65-1886-4366-bfb7-f415af9f4a40"))); // TODO: store and load roles properly
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
