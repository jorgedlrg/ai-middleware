package com.jorgedelarosa.aimiddleware.domain.session;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

// TODO: this should belong to an user
/**
 * @author jorge
 */
public class Session extends AggregateRoot {

  private final UUID scenario;
  private UUID currentContext;
  private final List<Interaction> interactions;
  private final Map<UUID, Performance> performances;
  private Locale locale;
  private Interaction lastInteraction;

  private Session(
      UUID scenario,
      UUID currentContext,
      List<Interaction> interactions,
      UUID id,
      Map<UUID, Performance> performances,
      Locale locale,
      Interaction lastInteraction) {
    super(Session.class, id);
    this.scenario = scenario;
    this.interactions = interactions;
    this.currentContext = currentContext;
    this.performances = performances;
    this.locale = locale;
    this.lastInteraction = lastInteraction;
  }

  // TODO: Performances shouldn't be added this way. Do it with a method addPerformance, and apply
  // rules (I.E. not having the same actor for different roles)
  public static Session create(
      UUID scenario, UUID currentContext, List<Performance> performances, Locale locale) {
    Map<UUID, Performance> map = new HashMap<>();
    for (Performance per : performances) {
      map.put(per.getRole(), per);
    }
    Session session =
        new Session(
            scenario, currentContext, new ArrayList<>(), UUID.randomUUID(), map, locale, null);
    session.validate();
    return session;
  }

  public static Session restore(
      UUID id,
      UUID scenario,
      UUID currentContext,
      List<Interaction> interactions,
      List<Performance> performances,
      Locale locale,
      Interaction lastInteraction) {
    Map<UUID, Performance> map = new HashMap<>();
    for (Performance per : performances) {
      map.put(per.getRole(), per);
    }
    Session session =
        new Session(
            scenario,
            currentContext,
            new ArrayList(interactions),
            id,
            map,
            locale,
            lastInteraction);
    session.validate();
    return session;
  }

  public void interact(String text, UUID role) {
    Performance performance = performances.get(role);
    if (performance != null) {
      UUID actorId = performance.getActor();
      Interaction newOne =
          Interaction.create(
              "", text, "", role, actorId, currentContext, Optional.ofNullable(lastInteraction));
      interactions.add(newOne);
      setLastInteraction(newOne);
    } else {
      throw new RuntimeException(String.format("Role %s not contained in performances.", role));
    }
    validate();
  }

  public void interactNext(String text, UUID role) {
    Performance performance = performances.get(role);
    if (performance != null) {
      UUID actorId = performance.getActor();
      Interaction parent = null;
      if (lastInteraction != null) {
        parent = lastInteraction.getParent().orElse(null);
      }
      Interaction newOne =
          Interaction.create(
              "", text, "", role, actorId, currentContext, Optional.ofNullable(parent));
      interactions.add(newOne);
      setLastInteraction(newOne);
    } else {
      throw new RuntimeException(String.format("Role %s not contained in performances.", role));
    }
    validate();
  }

  /**
   * Returns the youngest of the same level Interactions that happened BEFORE the last one. i.e.: A
   * [B] CURRENT D E
   *
   * @return
   */
  public Interaction getPreviousInteraction() throws NoSuchElementException {
    return interactions.stream()
        .filter(e -> e.getLevel().equals(lastInteraction.getLevel()))
        .filter(e -> e.getTimestamp().isBefore(lastInteraction.getTimestamp()))
        .sorted((p1, p2) -> p1.getTimestamp().compareTo(p2.getTimestamp()))
        .toList()
        .getLast();
  }

  /**
   * Returns the oldest of the same level Interactions that happened AFTER the last one. i.e.: A B
   * CURRENT [D] E
   *
   * @return
   */
  public Interaction getNextInteraction() throws NoSuchElementException {
    return interactions.stream()
        .filter(e -> e.getLevel().equals(lastInteraction.getLevel()))
        .filter(e -> e.getTimestamp().isAfter(lastInteraction.getTimestamp()))
        .sorted((p1, p2) -> p1.getTimestamp().compareTo(p2.getTimestamp()))
        .toList()
        .getFirst();
  }

  public void setLastInteraction(Interaction lastInteraction) {
    this.lastInteraction = lastInteraction;
    validate();
  }

  /**
   * FIXME: Since the interactions now are saved as a tree, this current method might leave orphan
   * Interactions. Purge orphan interactions
   *
   * @param interactionId
   */
  public void deleteInteraction(UUID interactionId) {
    for (int i = 0; i < interactions.size(); ++i) {
      if (interactions.get(i).getId().equals(interactionId)) {
        setLastInteraction(interactions.get(i).getParent().orElse(null));
        // TODO: purge orfan interactions in domain. don't do this with the DB, it wouldn't be DDD
        interactions.remove(i);
        break;
      }
    }
    validate();
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

  public List<Interaction> getCurrentInteractions() {
    // Get the current list of interactions, based on the current lastInteraction, and from there,
    // get the parent of each one
    List<Interaction> reversedInteractions = new ArrayList<>();
    for (Interaction current = lastInteraction;
        current != null;
        current = current.getParent().orElse(null)) {
      reversedInteractions.add(current);
    }

    return reversedInteractions.reversed();
  }

  public List<Interaction> getAllInteractions() {
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
    validate();
  }

  public void setCurrentContext(UUID currentContext) {
    this.currentContext = currentContext;
    validate();
  }

  public Interaction getLastInteraction() {
    return lastInteraction;
  }

  @Override
  public boolean validate() {
    if (scenario != null && currentContext != null && locale != null && !performances.isEmpty())
      return true;
    else
      throw new RuntimeException(
          String.format("%s %s not valid", this.getClass().getName(), getId()));
  }
}
