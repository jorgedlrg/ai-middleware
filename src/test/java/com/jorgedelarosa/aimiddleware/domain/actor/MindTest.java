package com.jorgedelarosa.aimiddleware.domain.actor;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class MindTest {

  @Test
  void create_shouldGenerateIdAndSetPersonality() {
    UUID actorId = UUID.randomUUID();
    Mind mind = Mind.create(actorId, "Friendly and outgoing");

    assertEquals(actorId, mind.getId());
    assertEquals("Friendly and outgoing", mind.getPersonality());
  }

  @Test
  void restore_shouldUseProvidedActorIdAndPersonality() {
    UUID actorId = UUID.randomUUID();
    Mind mind = Mind.restore(actorId, "Calm and collected");

    assertEquals(actorId, mind.getId());
    assertEquals("Calm and collected", mind.getPersonality());
  }

  @Test
  void setPersonality_shouldUpdatePersonality() {
    Mind mind = Mind.create(UUID.randomUUID(), "Old personality");
    mind.setPersonality("New personality");
    assertEquals("New personality", mind.getPersonality());
  }

  @Test
  void isValid_shouldReturnTrueForValidMind() {
    Mind mind = Mind.create(UUID.randomUUID(), "Valid personality");
    assertTrue(mind.isValid());
  }

  @Test
  void isValid_shouldReturnFalseWhenPersonalityIsNull() {
    Mind mind = Mind.create(UUID.randomUUID(), "Valid");
    mind.setPersonality(null);
    assertFalse(mind.isValid());
  }

  @Test
  void isValid_shouldReturnFalseWhenPersonalityIsBlank() {
    Mind mind = Mind.create(UUID.randomUUID(), "Valid");
    mind.setPersonality("   ");
    assertFalse(mind.isValid());
  }
}