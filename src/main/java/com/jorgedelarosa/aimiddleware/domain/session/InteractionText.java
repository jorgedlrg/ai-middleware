package com.jorgedelarosa.aimiddleware.domain.session;

import java.util.Optional;

/**
 * @author jorge
 */
public class InteractionText {
  private final String text;
  private final Optional<String> reasoning;

  public static Optional<InteractionText> optionalFromNullable(String text, String reasoning) {
    if (text != null) {
      return Optional.of(new InteractionText(text, Optional.ofNullable(reasoning)));
    } else {
      return Optional.empty();
    }
  }

  public InteractionText(String text, Optional<String> reasoning) {
    this.text = text.trim();
    this.reasoning = reasoning.map(e -> e.trim());
  }

  public String getText() {
    return text;
  }

  public Optional<String> getReasoning() {
    return reasoning;
  }
}
