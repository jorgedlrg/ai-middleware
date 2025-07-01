package com.jorgedelarosa.aimiddleware.domain;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Jorge
 */
@FunctionalInterface
public interface TwoArgsValidator<T, U> {
  boolean validate(T t, U u);

  default TwoArgsValidator<T, U> and(TwoArgsValidator<? super T, ? super U> other) {
    return (t, u) -> {
      boolean result = validate(t, u);
      return !result ? result : other.validate(t, u);
    };
  }

  default TwoArgsValidator<T, U> or(TwoArgsValidator<? super T, ? super U> other) {
    return (t, u) -> {
      boolean result = validate(t, u);
      return result ? result : other.validate(t, u);
    };
  }

  default TwoArgsValidator<T, U> negate() {
    return (t, u) -> {
      return !validate(t, u);
    };
  }

  public static final TwoArgsValidator<UUID, Collection<? extends Entity>> existsIn =
      (t, u) -> {
        if (t != null) {
          return u.stream().filter(e -> (e.getId()).equals(t)).findFirst().orElseThrow().validate();
        } else {
          return true;
        }
      };
}
