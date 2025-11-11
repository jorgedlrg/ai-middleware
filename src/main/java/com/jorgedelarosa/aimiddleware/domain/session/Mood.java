package com.jorgedelarosa.aimiddleware.domain.session;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jorge
 */
@Slf4j
public enum Mood {
  HAPPY("😊"),
  SAD("😢"),
  ANGRY("😠"),
  EXCITED("🤩"),
  NERVOUS("😰"),
  CALM("😌"),

  FLIRTY("😏"),
  PASSIONATE("🔥"),
  TENDER("🥰"),
  SEDUCTIVE("😈"),
  INTIMATE("💕"),
  AROUSED("💦"),

  DOMINANT("👑"),
  SUBMISSIVE("🙇"),
  CONFIDENT("💪"),
  SHY("🙈"),
  PLAYFUL("😜"),
  SERIOUS("🧐"),

  SURPRISED("😲"),
  CONFUSED("😵‍💫"),
  THOUGHTFUL("🤔"),
  FRUSTRATED("😤"),
  RELIEVED("😅"),
  CURIOUS("🤨"),

  TEASING("😋"),
  CARING("🤗"),
  MISCHIEVOUS("😼"),
  VULNERABLE("🥺"),
  EUPHORIC("🎉");

  private final String emoji;

  private Mood(String emoji) {
    this.emoji = emoji;
  }

  public String getEmoji() {
    return emoji;
  }

  public static Optional<Mood> optionalValueOf(String mood) {
    if (mood != null) {
      Mood result = null;
      try {
        result = Mood.valueOf(mood.toUpperCase());
      } catch (IllegalArgumentException e) {
        log.warn(String.format("%s is not a valid Mood value. Returning empty.", mood));
      }
      return Optional.ofNullable(result);
    } else {
      return Optional.empty();
    }
  }
}
