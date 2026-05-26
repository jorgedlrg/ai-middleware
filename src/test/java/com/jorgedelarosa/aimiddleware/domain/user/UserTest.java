package com.jorgedelarosa.aimiddleware.domain.user;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void create_shouldGenerateIdAndSetEmail() {
    User user = User.create("test@example.com");

    assertNotNull(user.getId());
    assertEquals("test@example.com", user.getEmail());
    assertNotNull(user.getSettings());
  }

  @Test
  void create_shouldCreateDefaultSettingsWithOllama() {
    User user = User.create("test@example.com");

    assertEquals("ollama", user.getSettings().getTextgenProvider());
  }

  @Test
  void restore_shouldUseProvidedValues() {
    UUID id = UUID.randomUUID();
    Settings settings = Settings.create(id, "openrouter");

    User user = User.restore(id, "user@example.com", settings);

    assertEquals(id, user.getId());
    assertEquals("user@example.com", user.getEmail());
    assertEquals(settings, user.getSettings());
  }

  @Test
  void getEmail_shouldReturnEmail() {
    User user = User.create("test@example.com");
    assertEquals("test@example.com", user.getEmail());
  }

  @Test
  void getSettings_shouldReturnSettings() {
    User user = User.create("test@example.com");
    Settings settings = user.getSettings();

    assertNotNull(settings);
    assertEquals("ollama", settings.getTextgenProvider());
  }

  @Test
  void getAggregateId_shouldReturnAggregateId() {
    User user = User.create("test@example.com");

    assertNotNull(user.getAggregateId());
    assertEquals(User.class, user.getAggregateId().getClazz());
    assertEquals(user.getId(), user.getAggregateId().getId());
  }

  @Test
  void isValid_shouldReturnTrueForValidUser() {
    User user = User.create("valid@example.com");
    assertTrue(user.isValid());
  }

  @Test
  void restore_shouldThrowWhenEmailIsNull() {
    UUID id = UUID.randomUUID();
    Settings settings = Settings.create(id, "ollama");
    assertThrows(RuntimeException.class, () -> User.restore(id, null, settings));
  }

  @Test
  void restore_shouldThrowWhenEmailIsBlank() {
    UUID id = UUID.randomUUID();
    Settings settings = Settings.create(id, "ollama");
    assertThrows(RuntimeException.class, () -> User.restore(id, "   ", settings));
  }

  @Test
  void restore_shouldThrowWhenSettingsIsNull() {
    UUID id = UUID.randomUUID();
    assertThrows(RuntimeException.class, () -> User.restore(id, "test@example.com", null));
  }
}