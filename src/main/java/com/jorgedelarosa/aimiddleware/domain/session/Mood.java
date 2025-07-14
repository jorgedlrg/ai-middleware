package com.jorgedelarosa.aimiddleware.domain.session;

/**
 * @author jorge
 */
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
}
