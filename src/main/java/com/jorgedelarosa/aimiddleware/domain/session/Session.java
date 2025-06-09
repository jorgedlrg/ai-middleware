package com.jorgedelarosa.aimiddleware.domain.session;

import com.jorgedelarosa.aimiddleware.domain.Actor;
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

  private Session(UUID scenario, List<Interaction> interactions, UUID id) {
    super(Session.class, id);
    this.scenario = scenario;
    this.interactions = interactions;
  }

  public static Session restore(UUID id, UUID scenario, List<Interaction> interactions) {
    return new Session(scenario, new ArrayList(interactions), id);
  }

  // FIXME: POC method. need to define current context. It doesn't necessarily has to be only 1
  // method
  public void interact(String text, Role role, Actor actor, boolean user) {
    interactions.add(
        Interaction.create(
            "",
            text,
            "",
            role.getId(),
            actor.getId(),
            user,
            UUID.fromString("af521f08-65f4-4171-9152-8e8e5c229ebf")));
  }

  public UUID getScenario() {
    return scenario;
  }

  public List<Interaction> getInteractions() {
    return interactions;
  }
}
