package com.jorgedelarosa.aimiddleware.domain.session;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class InteractionTest {

  @Test
  void create_shouldGenerateIdAndSetFields() {
    UUID roleId = UUID.randomUUID();
    UUID actorId = UUID.randomUUID();
    UUID contextId = UUID.randomUUID();
    InteractionText speech = new InteractionText("Hello!", Optional.empty());

    Interaction interaction = Interaction.create(
        Optional.of(new InteractionText("Thinking...", Optional.empty())),
        speech,
        Optional.of(new InteractionText("Waves hand", Optional.empty())),
        roleId,
        actorId,
        contextId,
        Optional.empty(),
        Optional.of(Mood.HAPPY));

    assertNotNull(interaction.getId());
    assertEquals(speech, interaction.getSpokenText());
    assertTrue(interaction.getThoughtText().isPresent());
    assertTrue(interaction.getActionText().isPresent());
    assertEquals(roleId, interaction.getRole());
    assertEquals(actorId, interaction.getActor());
    assertEquals(contextId, interaction.getContext());
    assertTrue(interaction.getParent().isEmpty());
    assertEquals(0, interaction.getLevel());
    assertTrue(interaction.getMood().isPresent());
    assertEquals(Mood.HAPPY, interaction.getMood().get());
    assertNotNull(interaction.getTimestamp());
  }

  @Test
  void create_shouldSetLevelToZeroWhenNoParent() {
    Interaction interaction = Interaction.create(
        Optional.empty(),
        new InteractionText("Speech", Optional.empty()),
        Optional.empty(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        Optional.empty(),
        Optional.empty());

    assertEquals(0, interaction.getLevel());
  }

  @Test
  void create_shouldSetLevelToParentLevelPlusOne() {
    UUID roleId = UUID.randomUUID();
    UUID actorId = UUID.randomUUID();
    UUID contextId = UUID.randomUUID();

    Interaction parent = Interaction.create(
        Optional.empty(),
        new InteractionText("Parent speech", Optional.empty()),
        Optional.empty(),
        roleId,
        actorId,
        contextId,
        Optional.empty(),
        Optional.empty());

    Interaction child = Interaction.create(
        Optional.empty(),
        new InteractionText("Child speech", Optional.empty()),
        Optional.empty(),
        roleId,
        actorId,
        contextId,
        Optional.of(parent),
        Optional.empty());

    assertEquals(0, parent.getLevel());
    assertEquals(1, child.getLevel());
  }

  @Test
  void restore_shouldUseProvidedValues() {
    UUID id = UUID.randomUUID();
    UUID roleId = UUID.randomUUID();
    UUID actorId = UUID.randomUUID();
    UUID contextId = UUID.randomUUID();
    long timestamp = System.currentTimeMillis() - 3600000;

    Interaction interaction = Interaction.restore(
        id,
        Optional.empty(),
        new InteractionText("Restored speech", Optional.empty()),
        Optional.empty(),
        timestamp,
        roleId,
        actorId,
        contextId,
        Optional.empty(),
        Optional.of(Mood.CALM));

    assertEquals(id, interaction.getId());
    assertEquals("Restored speech", interaction.getSpokenText().getText());
    assertEquals(timestamp, interaction.getTimestamp().toEpochMilli());
    assertEquals(0, interaction.getLevel());
  }

  @Test
  void restore_shouldCalculateLevelFromParent() {
    UUID roleId = UUID.randomUUID();
    UUID actorId = UUID.randomUUID();
    UUID contextId = UUID.randomUUID();

    Interaction parent = Interaction.create(
        Optional.empty(),
        new InteractionText("Parent", Optional.empty()),
        Optional.empty(),
        roleId,
        actorId,
        contextId,
        Optional.empty(),
        Optional.empty());

    long timestamp = System.currentTimeMillis();
    Interaction child = Interaction.restore(
        UUID.randomUUID(),
        Optional.empty(),
        new InteractionText("Child", Optional.empty()),
        Optional.empty(),
        timestamp,
        roleId,
        actorId,
        contextId,
        Optional.of(parent),
        Optional.empty());

    assertEquals(1, child.getLevel());
  }

  @Test
  void getSpokenText_shouldReturnSpeech() {
    InteractionText speech = new InteractionText("The quick brown fox", Optional.empty());
    Interaction interaction = Interaction.create(
        Optional.empty(),
        speech,
        Optional.empty(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        Optional.empty(),
        Optional.empty());

    assertEquals(speech, interaction.getSpokenText());
  }

  @Test
  void getParent_shouldReturnEmptyWhenNoParent() {
    Interaction interaction = Interaction.create(
        Optional.empty(),
        new InteractionText("Solo", Optional.empty()),
        Optional.empty(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        Optional.empty(),
        Optional.empty());

    assertTrue(interaction.getParent().isEmpty());
  }

  @Test
  void getMood_shouldReturnEmptyWhenNoMood() {
    Interaction interaction = Interaction.create(
        Optional.empty(),
        new InteractionText("Neutral", Optional.empty()),
        Optional.empty(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        Optional.empty(),
        Optional.empty());

    assertTrue(interaction.getMood().isEmpty());
  }

  @Test
  void isValid_shouldReturnTrueForValidInteraction() {
    Interaction interaction = Interaction.create(
        Optional.empty(),
        new InteractionText("Valid speech", Optional.empty()),
        Optional.empty(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        Optional.empty(),
        Optional.of(Mood.HAPPY));

    assertTrue(interaction.isValid());
  }

  @Test
  void create_shouldThrowWhenSpeechIsNull() {
    assertThrows(RuntimeException.class, () ->
        Interaction.create(
            Optional.empty(),
            null,
            Optional.empty(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            Optional.empty(),
            Optional.of(Mood.HAPPY)));
  }

  @Test
  void create_shouldThrowWhenRoleIsNull() {
    assertThrows(RuntimeException.class, () ->
        Interaction.create(
            Optional.empty(),
            new InteractionText("Speech", Optional.empty()),
            Optional.empty(),
            null,
            UUID.randomUUID(),
            UUID.randomUUID(),
            Optional.empty(),
            Optional.of(Mood.HAPPY)));
  }

  @Test
  void create_shouldThrowWhenActorIsNull() {
    assertThrows(RuntimeException.class, () ->
        Interaction.create(
            Optional.empty(),
            new InteractionText("Speech", Optional.empty()),
            Optional.empty(),
            UUID.randomUUID(),
            null,
            UUID.randomUUID(),
            Optional.empty(),
            Optional.of(Mood.HAPPY)));
  }

  @Test
  void create_shouldThrowWhenContextIsNull() {
    assertThrows(RuntimeException.class, () ->
        Interaction.create(
            Optional.empty(),
            new InteractionText("Speech", Optional.empty()),
            Optional.empty(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            null,
            Optional.empty(),
            Optional.of(Mood.HAPPY)));
  }
}