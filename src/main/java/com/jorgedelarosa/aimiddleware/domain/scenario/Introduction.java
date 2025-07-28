package com.jorgedelarosa.aimiddleware.domain.scenario;

import com.jorgedelarosa.aimiddleware.domain.Entity;
import com.jorgedelarosa.aimiddleware.domain.Validator;
import java.util.Optional;
import java.util.UUID;

/**
 * @author jorge
 */
public class Introduction extends Entity {

  private String spokenText;
  private Optional<String> thoughtText;
  private Optional<String> actionText;
  private final Role performer;
  private final Context context;

  private Introduction(
      String spokenText,
      Optional<String> thoughtText,
      Optional<String> actionText,
      UUID id,
      Role performer,
      Context context) {
    super(id);
    this.spokenText = spokenText;
    this.thoughtText = thoughtText;
    this.actionText = actionText;
    this.performer = performer;
    this.context = context;
  }

  public static Introduction restore(
      UUID id,
      String spokenText,
      Optional<String> thoughtText,
      Optional<String> actionText,
      Role performer,
      Context context) {
    Introduction introduction =
        new Introduction(spokenText, thoughtText, actionText, id, performer, context);
    introduction.validate();
    return introduction;
  }

  public static Introduction create(
      String spokenText,
      Optional<String> thoughtText,
      Optional<String> actionText,
      Role performer,
      Context context) {
    Introduction introduction =
        new Introduction(
            spokenText, thoughtText, actionText, UUID.randomUUID(), performer, context);
    introduction.validate();
    return introduction;
  }

  public String getSpokenText() {
    return spokenText;
  }

  public void setSpokenText(String spokenText) {
    this.spokenText = spokenText;
    validate();
  }

  public Optional<String> getThoughtText() {
    return thoughtText;
  }

  public void setThoughtText(Optional<String> thoughtText) {
    this.thoughtText = thoughtText;
    validate();
  }

  public Optional<String> getActionText() {
    return actionText;
  }

  public void setActionText(Optional<String> actionText) {
    this.actionText = actionText;
    validate();
  }

  public Role getPerformer() {
    return performer;
  }

  public Context getContext() {
    return context;
  }

  @Override
  public boolean validate() {
    return Validator.strNotEmpty.validate(spokenText) && performer != null && context != null;
  }
}
