package com.jorgedelarosa.aimiddleware.domain.session;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

// TODO: probably users might need permissions to use an interaction
/**
 * @author jorge
 */
public class Session extends AggregateRoot {

  private final UUID scenario;
  private UUID currentContext;
  private final List<Interaction> interactions;
  private final Map<UUID, Performance> performances;
  private Locale locale;
  private Interaction lastInteraction; // FIXME make optional

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

  public void interact(
      Optional<InteractionText> thoughts, Optional<InteractionText> action, InteractionText speech, UUID role, Optional<Mood> mood) {
    Performance performance = performances.get(role);
    if (performance != null) {
      UUID actorId = performance.getActor();
      Interaction newOne =
          Interaction.create(
              thoughts,
              speech,
              action,
              role,
              actorId,
              currentContext,
              Optional.ofNullable(lastInteraction),
              mood);
      interactions.add(newOne);
      setLastInteraction(newOne);
    } else {
      throw new RuntimeException(String.format("Role %s not contained in performances.", role));
    }
    validate();
  }

  public void interactNext(
      Optional<InteractionText> thoughts, Optional<InteractionText> action, InteractionText speech, UUID role, Optional<Mood> mood) {
    Performance performance = performances.get(role);
    if (performance != null) {
      UUID actorId = performance.getActor();
      Interaction parent = null;
      if (lastInteraction != null) {
        parent = lastInteraction.getParent().orElse(null);
      }
      Interaction newOne =
          Interaction.create(
              thoughts,
              speech,
              action,
              role,
              actorId,
              currentContext,
              Optional.ofNullable(parent),
              mood);
      interactions.add(newOne);
      setLastInteraction(newOne);
    } else {
      throw new RuntimeException(String.format("Role %s not contained in performances.", role));
    }
    validate();
  }

  /**
   * Returns the youngest of the same level Interactions that happened BEFORE the last one.
   *
   * <p>i.e.: A [B] CURRENT D E
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
   * Returns the oldest of the same level Interactions that happened AFTER the last one.
   *
   * <p>i.e.: A B CURRENT [D] E
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

  /**
   * FIXME: don't use a setter. this should be modified properly with other methods
   *
   * @param lastInteraction
   */
  public void setLastInteraction(Interaction lastInteraction) {
    this.lastInteraction = lastInteraction;
    validate();
  }

  public void deleteInteraction(UUID interactionId) {
    for (int i = 0; i < interactions.size(); ++i) {
      if (interactions.get(i).getId().equals(interactionId)) {
        Interaction parent = interactions.get(i).getParent().orElse(null);
        // (recursively) Delete first its children
        getChildren(interactions.get(i)).stream().forEach(e -> deleteInteraction(e.getId()));
        // Now delete the interaction
        lastInteraction = parent;

        if (lastInteraction == null) {
          // If we're going to remove the first interaction, remove the remaining interactions
          interactions.clear();
        } else {
          interactions.remove(i);
        }
        break;
      }
    }
    validate();
  }

  public void addPerformance(Performance performance) {
    performances.put(performance.getRole(), performance);
    validate();
  }

  public void replacePerformances(List<Performance> performances) {
    this.performances.clear();
    performances.stream().forEach(p -> addPerformance(p));
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

  public List<Interaction> getChildren(Interaction parent) {
    // The first interactions have parent==null, therefore we use Objects.equals()
    return interactions.stream()
        .filter(e -> Objects.equals(e.getParent().orElse(null), parent))
        .toList();
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
    if (scenario != null
        && currentContext != null
        && locale != null
        && !performances.isEmpty()
        && ((interactions.isEmpty() && lastInteraction == null)
            || (!interactions.isEmpty() && lastInteraction != null))) {
      return true;
    } else
      throw new RuntimeException(
          String.format("%s %s not valid", this.getClass().getName(), getId()));
  }
}
