package com.jorgedelarosa.aimiddleware.domain.session;

import java.util.Optional;

/**
 * @author jorge
 */
public class InteractionText {

  private final String text;
  private final Optional<String> reasoning;

  /**
   * Creates optional interaction from a nullable or blank text input
   * @param text
   * @param reasoning
   * @return 
   */
  public static Optional<InteractionText> optionalFromNullable(String text, String reasoning) {
    if (text != null && !text.isBlank()) {
      return Optional.of(new InteractionText(text, Optional.ofNullable(reasoning)));
    } else {
      return Optional.empty();
    }
  }

  public InteractionText(String text, Optional<String> reasoning) {
    this.text = text.trim();
    if (text.isBlank()) {
      throw new RuntimeException("Interaction text can't be blank");
    }
    this.reasoning = reasoning.map(e -> e.trim());
  }

  public String getText() {
    return text;
  }

  public Optional<String> getReasoning() {
    return reasoning;
  }
}
