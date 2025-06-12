package com.jorgedelarosa.aimiddleware.domain.session;

import com.jorgedelarosa.aimiddleware.domain.Actor;
import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author jorge
 */
public class Session extends AggregateRoot {

  private final UUID scenario;
  private final UUID currentContext;
  private final List<Interaction> interactions;
  private final Map<UUID, Performance> performances;

  private Session(
      UUID scenario,
      UUID currentContext,
      List<Interaction> interactions,
      UUID id,
      Map<UUID, Performance> performances) {
    super(Session.class, id);
    this.scenario = scenario;
    this.interactions = interactions;
    this.currentContext = currentContext;
    this.performances = performances;
  }

  public static Session restore(
      UUID id,
      UUID scenario,
      UUID currentContext,
      List<Interaction> interactions,
      List<Performance> performances) {
    Map<UUID, Performance> map = new HashMap<>();
    performances.stream().map((e) -> map.put(e.getRole(), e));
    return new Session(scenario, currentContext, new ArrayList(interactions), id, map);
  }

  // FIXME: POC method.It doesn't necessarily has to be only 1
  // method
  public void interact(String text, Role role, Actor actor, boolean user) {
    interactions.add(
        Interaction.create("", text, "", role.getId(), actor.getId(), user, currentContext));
  }

  public UUID getScenario() {
    return scenario;
  }

  public UUID getCurrentContext() {
    return currentContext;
  }

  public List<Interaction> getInteractions() {
    return interactions;
  }

  public Map<UUID, Performance> getPerformances() {
    return performances;
  }
}
