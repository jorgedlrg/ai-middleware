package com.jorgedelarosa.aimiddleware.domain.session;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// TODO: this should belong to an user
/**
 * @author jorge
 */
public class Session extends AggregateRoot {

  private final UUID scenario;
  private final UUID currentContext;
  private final List<Interaction> interactions;
  private final Map<UUID, Performance> performances;
  private Locale locale;

  private Session(
      UUID scenario,
      UUID currentContext,
      List<Interaction> interactions,
      UUID id,
      Map<UUID, Performance> performances,
      Locale locale) {
    super(Session.class, id);
    this.scenario = scenario;
    this.interactions = interactions;
    this.currentContext = currentContext;
    this.performances = performances;
    this.locale = locale;
  }

  public static Session create(
      UUID scenario, UUID currentContext, List<Performance> performances, Locale locale) {
    Map<UUID, Performance> map = new HashMap<>();
    for (Performance per : performances) {
      map.put(per.getRole(), per);
    }
    return new Session(scenario, currentContext, new ArrayList<>(), UUID.randomUUID(), map, locale);
  }

  public static Session restore(
      UUID id,
      UUID scenario,
      UUID currentContext,
      List<Interaction> interactions,
      List<Performance> performances,
      Locale locale) {
    Map<UUID, Performance> map = new HashMap<>();
    for (Performance per : performances) {
      map.put(per.getRole(), per);
    }
    return new Session(scenario, currentContext, new ArrayList(interactions), id, map, locale);
  }

  public void interact(String text, UUID role, boolean user) {
    Performance performance = performances.get(role);
    if (performance != null) {
      UUID actorId = performance.getActor();
      interactions.add(Interaction.create("", text, "", role, actorId, user, currentContext));
    }
    // TODO: else warn or something
  }

  public void deleteInteraction(UUID interactionId) {
    for (int i = 0; i < interactions.size(); ++i) {
      if (interactions.get(i).getId().equals(interactionId)) {
        interactions.remove(i);
        break;
      }
    }
  }

  public List<UUID> getFeaturedActors() {
    return performances.values().stream().map(e -> e.getActor()).toList();
  }

  public Optional<UUID> getFeaturedActor(UUID role) {
    return Optional.ofNullable(performances.get(role)).map(e -> e.getActor());
  }

  public UUID getScenario() {
    return scenario;
  }

  public UUID getCurrentContext() {
    return currentContext;
  }

  public List<Interaction> getInteractions() {
    return List.copyOf(interactions);
  }

  public List<Performance> getPerformances() {
    return List.copyOf(performances.values());
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }
}
