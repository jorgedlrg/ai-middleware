package com.jorgedelarosa.aimiddleware.domain.session;

import java.util.Optional;

/**
 * @author jorge
 */
public class InteractionText {
  private final String text;
  private final Optional<String> reasoning;

  public InteractionText(String text, Optional<String> reasoning) {
    this.text = text;
    this.reasoning = reasoning;
  }

  public String getText() {
    return text;
  }

  public Optional<String> getReasoning() {
    return reasoning;
  }
}
