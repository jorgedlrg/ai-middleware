package com.jorgedelarosa.aimiddleware.domain.user;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import com.jorgedelarosa.aimiddleware.domain.Validator;
import java.util.UUID;

/**
 * @author jorge
 */
public class User extends AggregateRoot {

  private final String email;

  private User(UUID id, String email) {
    super(User.class, id);
    this.email = email;
    validate();
  }

  public static User restore(UUID id, String email) {
    return new User(id, email);
  }

  public static User create(String email) {
    return new User(UUID.randomUUID(), email);
  }

  public String getEmail() {
    return email;
  }

  @Override
  public final boolean validate() {
    // TODO: validate email format
    return Validator.strNotEmpty.validate(email);
  }
}
