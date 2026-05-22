package com.jorgedelarosa.aimiddleware.domain.actor;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MemoryFragmentTest {

  @Test
  void create_shouldGenerateIdAndSetText() {
    MemoryFragment fragment = MemoryFragment.create("Test memory text");

    assertNotNull(fragment.getId());
    assertEquals("Test memory text", fragment.getText());
  }

  @Test
  void create_shouldSetTimestampToNow() {
    Instant before = Instant.now();
    MemoryFragment fragment = MemoryFragment.create("Test");
    Instant after = Instant.now();

    assertTrue(fragment.getTimestamp().compareTo(before) >= 0);
    assertTrue(fragment.getTimestamp().compareTo(after) <= 0);
  }

  @Test
  void create_shouldSetEnabledToTrue() {
    MemoryFragment fragment = MemoryFragment.create("Test");
    assertTrue(fragment.isEnabled());
  }

  @Test
  void restore_shouldUseProvidedValues() {
    UUID id = UUID.randomUUID();
    Instant timestamp = Instant.now().minusSeconds(3600);

    MemoryFragment fragment = MemoryFragment.restore("Restored text", timestamp, id, false);

    assertEquals(id, fragment.getId());
    assertEquals("Restored text", fragment.getText());
    assertEquals(timestamp, fragment.getTimestamp());
    assertFalse(fragment.isEnabled());
  }

  @Test
  void setText_shouldUpdateText() {
    MemoryFragment fragment = MemoryFragment.create("Original");
    fragment.setText("Updated");

    assertEquals("Updated", fragment.getText());
  }

  @Test
  void setText_shouldThrowWhenBlank() {
    MemoryFragment fragment = MemoryFragment.create("Original");
    assertThrows(RuntimeException.class, () -> fragment.setText(""));
  }

  @Test
  void setText_shouldThrowWhenNull() {
    MemoryFragment fragment = MemoryFragment.create("Original");
    assertThrows(RuntimeException.class, () -> fragment.setText(null));
  }

  @Test
  void setEnabled_shouldUpdateFlag() {
    MemoryFragment fragment = MemoryFragment.create("Test");
    assertTrue(fragment.isEnabled());

    fragment.setEnabled(false);
    assertFalse(fragment.isEnabled());

    fragment.setEnabled(true);
    assertTrue(fragment.isEnabled());
  }

  @Test
  void getTimestamp_shouldReturnSetTimestamp() {
    Instant timestamp = Instant.now();
    MemoryFragment fragment = MemoryFragment.restore("Test", timestamp, UUID.randomUUID(), true);
    assertEquals(timestamp, fragment.getTimestamp());
  }

  @Test
  void isValid_shouldReturnTrueForValidFragment() {
    MemoryFragment fragment = MemoryFragment.create("Valid text");
    assertTrue(fragment.isValid());
  }
}