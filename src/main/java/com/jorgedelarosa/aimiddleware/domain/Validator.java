package com.jorgedelarosa.aimiddleware.domain;

/**
 * @author
 *     https://medium.com/walmartglobaltech/fluent-validation-with-chaining-methods-in-java-improving-code-readability-and-flexibility-beb8b040a2ee
 */
@FunctionalInterface
public interface Validator<T> {
  boolean validate(T t);

  default Validator<T> and(Validator<? super T> other) {
    return t -> {
      boolean result = validate(t);
      return !result ? result : other.validate(t);
    };
  }

  default Validator<T> or(Validator<? super T> other) {
    return t -> {
      boolean result = validate(t);
      return result ? result : other.validate(t);
    };
  }

  default Validator<T> negate() {
    return t -> {
      return !validate(t);
    };
  }

  public static final Validator<String> strNotEmpty =
      str -> {
        if (str == null || str.equals("")) {
          throw new RuntimeException("shouldn't be empty");
        }
        return true;
      };
}
