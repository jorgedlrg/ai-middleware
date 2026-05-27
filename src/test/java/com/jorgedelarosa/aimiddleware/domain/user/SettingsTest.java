package com.jorgedelarosa.aimiddleware.domain.user;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class SettingsTest {

  @Test
  void create_shouldSetIdAndProvider() {
    UUID userId = UUID.randomUUID();
    Settings settings = Settings.create(userId, "ollama");

    assertEquals(userId, settings.getId());
    assertEquals("ollama", settings.getTextgenProvider());
  }

  @Test
  void create_shouldSetDefaultFlags() {
    UUID userId = UUID.randomUUID();
    Settings settings = Settings.create(userId, "openrouter");

    assertTrue(settings.isActionsEnabled());
    assertTrue(settings.isMoodEnabled());
    assertTrue(settings.isThoughtsEnabled());
    assertFalse(settings.isActionsReasoning());
    assertFalse(settings.isSpeechReasoning());
    assertFalse(settings.isThoughtsReasoning());
  }

  @Test
  void restore_shouldUseProvidedValues() {
    UUID userId = UUID.randomUUID();
    Settings settings = Settings.restore(
        userId,
        "openrouter",
        "api-key-123",
        "model-xyz",
        "http://localhost:11434",
        "llama2",
        "localhost:8188",
        true,
        false,
        true,
        true,
        false,
        true);

    assertEquals(userId, settings.getId());
    assertEquals("openrouter", settings.getTextgenProvider());
    assertEquals("api-key-123", settings.getOpenrouterApikey());
    assertEquals("model-xyz", settings.getOpenrouterModel());
    assertEquals("http://localhost:11434", settings.getOllamaHost());
    assertEquals("llama2", settings.getOllamaModel());
    assertEquals("localhost:8188", settings.getComfyUiHost());
    assertTrue(settings.isActionsEnabled());
    assertFalse(settings.isMoodEnabled());
    assertTrue(settings.isThoughtsEnabled());
    assertTrue(settings.isActionsReasoning());
    assertFalse(settings.isSpeechReasoning());
    assertTrue(settings.isThoughtsReasoning());
  }

  @Test
  void setOpenrouterApikey_shouldUpdateKey() {
    UUID userId = UUID.randomUUID();
    Settings settings = Settings.create(userId, "openrouter");

    settings.setOpenrouterApikey("new-key");

    assertEquals("new-key", settings.getOpenrouterApikey());
  }

  @Test
  void setOpenrouterModel_shouldUpdateModel() {
    UUID userId = UUID.randomUUID();
    Settings settings = Settings.create(userId, "openrouter");

    settings.setOpenrouterModel("new-model");

    assertEquals("new-model", settings.getOpenrouterModel());
  }

  @Test
  void setOllamaHost_shouldUpdateHost() {
    UUID userId = UUID.randomUUID();
    Settings settings = Settings.create(userId, "ollama");

    settings.setOllamaHost("http://localhost:11434");

    assertEquals("http://localhost:11434", settings.getOllamaHost());
  }

  @Test
  void setOllamaModel_shouldUpdateModel() {
    UUID userId = UUID.randomUUID();
    Settings settings = Settings.create(userId, "ollama");

    settings.setOllamaModel("mistral");

    assertEquals("mistral", settings.getOllamaModel());
  }

  @Test
  void setActionsEnabled_shouldUpdateFlag() {
    UUID userId = UUID.randomUUID();
    Settings settings = Settings.create(userId, "ollama");

    settings.setActionsEnabled(false);

    assertFalse(settings.isActionsEnabled());
  }

  @Test
  void setMoodEnabled_shouldUpdateFlag() {
    UUID userId = UUID.randomUUID();
    Settings settings = Settings.create(userId, "ollama");

    settings.setMoodEnabled(false);

    assertFalse(settings.isMoodEnabled());
  }

  @Test
  void setThoughtsEnabled_shouldUpdateFlag() {
    UUID userId = UUID.randomUUID();
    Settings settings = Settings.create(userId, "ollama");

    settings.setThoughtsEnabled(false);

    assertFalse(settings.isThoughtsEnabled());
  }

  @Test
  void setActionsReasoning_shouldUpdateFlag() {
    UUID userId = UUID.randomUUID();
    Settings settings = Settings.create(userId, "ollama");

    settings.setActionsReasoning(true);

    assertTrue(settings.isActionsReasoning());
  }

  @Test
  void setSpeechReasoning_shouldUpdateFlag() {
    UUID userId = UUID.randomUUID();
    Settings settings = Settings.create(userId, "ollama");

    settings.setSpeechReasoning(true);

    assertTrue(settings.isSpeechReasoning());
  }

  @Test
  void setThoughtsReasoning_shouldUpdateFlag() {
    UUID userId = UUID.randomUUID();
    Settings settings = Settings.create(userId, "ollama");

    settings.setThoughtsReasoning(true);

    assertTrue(settings.isThoughtsReasoning());
  }

  @Test
  void isValid_shouldReturnTrueForOpenrouterProvider() {
    UUID userId = UUID.randomUUID();
    Settings settings = Settings.create(userId, "openrouter");
    assertTrue(settings.isValid());
  }

  @Test
  void isValid_shouldReturnTrueForOllamaProvider() {
    UUID userId = UUID.randomUUID();
    Settings settings = Settings.create(userId, "ollama");
    assertTrue(settings.isValid());
  }

  @Test
  void create_shouldThrowWhenProviderIsInvalid() {
    UUID userId = UUID.randomUUID();
    assertThrows(RuntimeException.class, () -> Settings.create(userId, "anthropic"));
  }

  @Test
  void create_shouldThrowWhenProviderIsNull() {
    UUID userId = UUID.randomUUID();
    assertThrows(RuntimeException.class, () -> Settings.create(userId, null));
  }

  @Test
  void create_shouldThrowWhenProviderIsBlank() {
    UUID userId = UUID.randomUUID();
    assertThrows(RuntimeException.class, () -> Settings.create(userId, "   "));
  }

  @Test
  void setTextgenProvider_shouldUpdateProvider() {
    UUID userId = UUID.randomUUID();
    Settings settings = Settings.create(userId, "ollama");

    settings.setTextgenProvider("openrouter");

    assertEquals("openrouter", settings.getTextgenProvider());
  }

  @Test
  void setTextgenProvider_shouldNotTriggerValidation() {
    UUID userId = UUID.randomUUID();
    Settings settings = Settings.create(userId, "ollama");

    settings.setTextgenProvider("openrouter");

    assertTrue(settings.isValid());
  }
}