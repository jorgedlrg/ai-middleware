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
  private final UUID owner;

  private MemoryFragment(String text, Instant timestamp, UUID owner, UUID id) {
    super(id);
    this.text = text;
    this.timestamp = timestamp;
    this.owner = owner;
    validate();
  }

  public static MemoryFragment create(String text, UUID owner) {
    return new MemoryFragment(text, Instant.now(), owner, UUID.randomUUID());
  }

  public static MemoryFragment restore(String text, Instant timestamp, UUID owner, UUID id) {
    return new MemoryFragment(text, timestamp, owner, id);
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

  public UUID getOwner() {
    return owner;
  }

  @Override
  public final boolean validate() {
    return Validator.strNotEmpty.validate(text) && timestamp != null && owner != null;
  }
}
