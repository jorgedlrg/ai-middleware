package com.jorgedelarosa.aimiddleware.domain.user;

import com.jorgedelarosa.aimiddleware.domain.Entity;
import com.jorgedelarosa.aimiddleware.domain.Validator;
import java.util.UUID;

/**
 * @author jorge
 */
public class Settings extends Entity {

  private String textgenProvider;
  private String openrouterApikey;
  private String openrouterModel;
  private String ollamaHost;
  private String ollamaModel;
  private boolean actionsEnabled;
  private boolean moodEnabled;
  private boolean thoughtsEnabled;

  private Settings(UUID id, String textgenProvider) {
    super(id);
    this.textgenProvider = textgenProvider;
    actionsEnabled = true;
    moodEnabled = true;
    thoughtsEnabled = true;
    validate();
  }

  public static Settings restore(
      UUID user,
      String textgenProvider,
      String openrouterApikey,
      String openrouterModel,
      String ollamaHost,
      String ollamaModel,
      boolean actionsEnabled,
      boolean moodEnabled,
      boolean thoughtsEnabled) {
    Settings settings = new Settings(user, textgenProvider);
    settings.setActionsEnabled(actionsEnabled);
    settings.setMoodEnabled(moodEnabled);
    settings.setOllamaHost(ollamaHost);
    settings.setOllamaModel(ollamaModel);
    settings.setOpenrouterApikey(openrouterApikey);
    settings.setOpenrouterModel(openrouterModel);
    settings.setThoughtsEnabled(thoughtsEnabled);
    settings.validate();
    return settings;
  }

  public static Settings create(UUID user, String textgenProvider) {
    return new Settings(user, textgenProvider);
  }

  public void setTextgenProvider(String textgenProvider) {
    this.textgenProvider = textgenProvider;
  }

  public void setOpenrouterApikey(String openrouterApikey) {
    this.openrouterApikey = openrouterApikey;
    validate();
  }

  public void setOpenrouterModel(String openrouterModel) {
    this.openrouterModel = openrouterModel;
    validate();
  }

  public void setOllamaHost(String ollamaHost) {
    this.ollamaHost = ollamaHost;
    validate();
  }

  public void setOllamaModel(String ollamaModel) {
    this.ollamaModel = ollamaModel;
    validate();
  }

  public void setActionsEnabled(boolean actionsEnabled) {
    this.actionsEnabled = actionsEnabled;
    validate();
  }

  public void setMoodEnabled(boolean moodEnabled) {
    this.moodEnabled = moodEnabled;
    validate();
  }

  public void setThoughtsEnabled(boolean thoughtsEnabled) {
    this.thoughtsEnabled = thoughtsEnabled;
    validate();
  }

  public String getTextgenProvider() {
    return textgenProvider;
  }

  public String getOpenrouterApikey() {
    return openrouterApikey;
  }

  public String getOpenrouterModel() {
    return openrouterModel;
  }

  public String getOllamaHost() {
    return ollamaHost;
  }

  public String getOllamaModel() {
    return ollamaModel;
  }

  public boolean isActionsEnabled() {
    return actionsEnabled;
  }

  public boolean isMoodEnabled() {
    return moodEnabled;
  }

  public boolean isThoughtsEnabled() {
    return thoughtsEnabled;
  }

  @Override
  public final boolean validate() {
    return Validator.strNotEmpty.validate(textgenProvider)
        && (textgenProvider.equals("openrouter") || textgenProvider.equals("ollama"));
  }
}
