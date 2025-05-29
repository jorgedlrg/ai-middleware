package com.jorgedelarosa.aimiddleware.domain.session;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author jorge
 */
public class Session extends AggregateRoot {

  private final UUID scenario;
  private final List<Interaction> interactions;

  private Session(UUID scenario, List<Interaction> interactions, Class clazz, UUID id) {
    super(clazz, id);
    this.scenario = scenario;
    this.interactions = interactions;
  }

  public Session(UUID scenario) {
    super(Session.class, UUID.randomUUID());
    this.scenario = scenario;
    this.interactions = new ArrayList<>();
  }

  public static Session restore(UUID id, UUID scenario, List<Interaction> interactions) {
    return new Session(scenario, new ArrayList(interactions), Session.class, id);
  }

  // POC method
  public void interact(String text, Role role) {
    interactions.add(new Interaction("", text, "", role.getId()));
  }

  public UUID getScenario() {
    return scenario;
  }

  public List<Interaction> getInteractions() {
    return interactions;
  }
}
