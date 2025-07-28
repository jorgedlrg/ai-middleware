package com.jorgedelarosa.aimiddleware.domain.scenario;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import com.jorgedelarosa.aimiddleware.domain.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author jorge
 */
public class Scenario extends AggregateRoot {

  private String name;
  private String description;
  private final List<Context> contexts;
  private final List<Role> roles;
  private final List<Introduction> introductions;

  private Scenario(
      String name,
      String description,
      List<Context> contexts,
      List<Role> roles,
      List<Introduction> introductions,
      Class clazz,
      UUID id) {
    super(clazz, id);
    this.name = name;
    this.description = description;
    this.contexts = contexts;
    this.roles = roles;
    this.introductions = introductions;
  }

  public static Scenario create(String name, String description) {
    Scenario scenario =
        new Scenario(
            name,
            description,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            Scenario.class,
            UUID.randomUUID());
    scenario.validate();
    return scenario;
  }

  public static Scenario restore(
      UUID id,
      String name,
      String description,
      List<Context> contexts,
      List<Role> roles,
      List<Introduction> introductions) {
    Scenario scenario =
        new Scenario(
            name,
            description,
            new ArrayList(contexts),
            new ArrayList(roles),
            new ArrayList(introductions),
            Scenario.class,
            id);
    scenario.validate();
    return scenario;
  }

  public void addNewContext(String name, String physicalDescription) {
    contexts.add(Context.create(name, physicalDescription));
    validate();
  }

  public void modifyContext(UUID contextId, String name, String physicalDescription) {
    contexts.stream()
        .filter(e -> e.getId().equals(contextId))
        .forEach(
            e -> {
              e.setName(name);
              e.setPhysicalDescription(physicalDescription);
            });
    validate();
  }

  public void deleteContext(UUID contextId) {
    for (int i = 0; i < contexts.size(); ++i) {
      if (contexts.get(i).getId().equals(contextId)) {
        contexts.remove(i);
        break;
      }
    }
    validate();
  }

  public void addNewRole(String name, String details) {
    roles.add(Role.create(name, details));
    validate();
  }

  public void modifyRole(UUID roleId, String name, String details) {
    roles.stream()
        .filter(e -> e.getId().equals(roleId))
        .forEach(
            e -> {
              e.setName(name);
              e.setDetails(details);
            });
    validate();
  }

  public void deleteRole(UUID roleId) {
    for (int i = 0; i < roles.size(); ++i) {
      if (roles.get(i).getId().equals(roleId)) {
        roles.remove(i);
        break;
      }
    }
    validate();
  }

  public void addNewIntroduction(
      String spokenText,
      Optional<String> thoughtText,
      Optional<String> actionText,
      Role performer,
      Context context) {
    introductions.add(Introduction.create(spokenText, thoughtText, actionText, performer, context));
    validate();
  }

  public void modifyIntroduction(
      UUID introductionId,
      String spokenText,
      Optional<String> thoughtText,
      Optional<String> actionText) {
    introductions.stream()
        .filter(e -> e.getId().equals(introductionId))
        .forEach(
            e -> {
              e.setActionText(actionText);
              e.setSpokenText(spokenText);
              e.setThoughtText(thoughtText);
            });
    validate();
  }

  public void deleteIntroduction(UUID introductionId) {
    for (int i = 0; i < introductions.size(); ++i) {
      if (introductions.get(i).getId().equals(introductionId)) {
        introductions.remove(i);
        break;
      }
    }
    validate();
  }

  public String getName() {
    return name;
  }

  public List<Context> getContexts() {
    return List.copyOf(contexts);
  }

  public List<Role> getRoles() {
    return List.copyOf(roles);
  }

  public List<Introduction> getIntroductions() {
    return List.copyOf(introductions);
  }

  public void setName(String name) {
    this.name = name;
    validate();
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean validate() {
    if (Validator.strNotEmpty.validate(name) && Validator.strNotEmpty.validate(description))
      return true;
    else
      throw new RuntimeException(
          String.format("%s %s not valid", this.getClass().getName(), getId()));
  }
}
