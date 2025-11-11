package com.jorgedelarosa.aimiddleware.domain.user;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import com.jorgedelarosa.aimiddleware.domain.Validator;
import java.util.UUID;

/**
 * @author jorge
 */
public class User extends AggregateRoot {

  private final String email;
  private final Settings settings;

  private User(UUID id, String email, Settings settings) {
    super(User.class, id);
    this.email = email;
    this.settings = settings;
    validate();
  }

  public static User restore(UUID id, String email, Settings settings) {
    return new User(id, email, settings);
  }

  public static User create(String email) {
    UUID userId = UUID.randomUUID();
    return new User(userId, email, Settings.create(userId, "ollama"));
  }

  public String getEmail() {
    return email;
  }

  public Settings getSettings() {
    return settings;
  }

  @Override
  public final boolean validate() {
    // TODO: validate email format
    return Validator.strNotEmpty.validate(email) && settings != null;
  }
}
