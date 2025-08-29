package com.jorgedelarosa.aimiddleware.domain.actor;

import com.jorgedelarosa.aimiddleware.domain.Entity;
import com.jorgedelarosa.aimiddleware.domain.Validator;
import java.time.Instant;
import java.util.UUID;

/**
 * @author jorge
 */
public class MemoryFragment extends Entity {

  private String text;
  private final Instant timestamp;
  private boolean enabled;

  private MemoryFragment(String text, Instant timestamp, UUID id, boolean enabled) {
    super(id);
    this.text = text;
    this.timestamp = timestamp;
    this.enabled = enabled;
    validate();
  }

  public static MemoryFragment create(String text) {
    return new MemoryFragment(text, Instant.now(), UUID.randomUUID(), true);
  }

  public static MemoryFragment restore(String text, Instant timestamp, UUID id, boolean enabled) {
    return new MemoryFragment(text, timestamp, id, enabled);
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
    validate();
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
    validate();
  }

  @Override
  public final boolean validate() {
    return Validator.strNotEmpty.validate(text) && timestamp != null;
  }
}
