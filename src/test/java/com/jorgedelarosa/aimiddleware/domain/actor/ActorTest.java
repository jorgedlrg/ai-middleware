package com.jorgedelarosa.aimiddleware.domain.actor;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ActorTest {

  @Test
  void create_shouldGenerateIdAndSetFields() {
    Actor actor = Actor.create("John", "A mysterious figure", "Tall with dark hair", "Calm and collected");

    assertNotNull(actor.getId());
    assertEquals("John", actor.getName());
    assertEquals("A mysterious figure", actor.getProfile());
    assertEquals("Tall with dark hair", actor.getPhysicalDescription());
    assertTrue(actor.getMind().isPresent());
    assertEquals("Calm and collected", actor.getMind().get().getPersonality());
    assertTrue(actor.getCurrentOutfit().isEmpty());
    assertNotNull(actor.getCreatedAt());
    assertNotNull(actor.getUpdatedAt());
  }

  @Test
  void create_shouldSetCreatedAtAndUpdatedAtToNow() {
    Instant before = Instant.now();
    Actor actor = Actor.create("John", "Profile", "Description", "Personality");
    Instant after = Instant.now();

    assertTrue(actor.getCreatedAt().compareTo(before) >= 0);
    assertTrue(actor.getCreatedAt().compareTo(after) <= 0);
    assertTrue(actor.getUpdatedAt().compareTo(before) >= 0);
    assertTrue(actor.getUpdatedAt().compareTo(after) <= 0);
  }

  @Test
  void create_shouldNotCreateMindWhenPersonalityIsNull() {
    Actor actor = Actor.create("John", "Profile", "Description", null);
    assertTrue(actor.getMind().isEmpty());
  }

  @Test
  void create_shouldThrowWhenNameIsBlank() {
    assertThrows(RuntimeException.class, () ->
        Actor.create("", "Profile", "Description", "Personality"));
  }

  @Test
  void create_shouldThrowWhenProfileIsBlank() {
    assertThrows(RuntimeException.class, () ->
        Actor.create("John", "", "Description", "Personality"));
  }

  @Test
  void create_shouldThrowWhenPhysicalDescriptionIsBlank() {
    assertThrows(RuntimeException.class, () ->
        Actor.create("John", "Profile", "", "Personality"));
  }

  @Test
  void restore_shouldUseProvidedIdAndSetFields() {
    UUID id = UUID.randomUUID();
    Instant created = Instant.now().minusSeconds(3600);
    Instant updated = Instant.now();
    Mind mind = Mind.create(id, "Personality");

    Actor actor = Actor.restore(
        id, "John", "Profile", "Description", Optional.of(mind), Optional.of(UUID.randomUUID()), created, updated);

    assertEquals(id, actor.getId());
    assertEquals("John", actor.getName());
    assertEquals("Profile", actor.getProfile());
    assertEquals("Description", actor.getPhysicalDescription());
    assertTrue(actor.getMind().isPresent());
    assertEquals(mind, actor.getMind().get());
  }

  @Test
  void chooseOutfit_shouldSetCurrentOutfit() {
    Actor actor = Actor.create("John", "Profile", "Description", null);
    UUID outfitId = UUID.randomUUID();

    actor.chooseOutfit(outfitId);

    assertTrue(actor.getCurrentOutfit().isPresent());
    assertEquals(outfitId, actor.getCurrentOutfit().get());
  }

  @Test
  void chooseOutfit_shouldAllowNullToClearOutfit() {
    UUID outfitId = UUID.randomUUID();
    Actor actor = Actor.create("John", "Profile", "Description", null);
    actor.chooseOutfit(outfitId);

    actor.chooseOutfit(null);

    assertTrue(actor.getCurrentOutfit().isEmpty());
  }

  @Test
  void setPersonality_shouldUpdateExistingMind() {
    Actor actor = Actor.create("John", "Profile", "Description", "Old personality");
    Mind existingMind = actor.getMind().get();

    actor.setPersonality("New personality");

    assertEquals("New personality", actor.getMind().get().getPersonality());
    assertSame(existingMind, actor.getMind().get());
  }

  @Test
  void setPersonality_shouldCreateMindWhenNoneExists() {
    Actor actor = Actor.create("John", "Profile", "Description", null);
    assertTrue(actor.getMind().isEmpty());

    actor.setPersonality("New personality");

    assertTrue(actor.getMind().isPresent());
    assertEquals("New personality", actor.getMind().get().getPersonality());
  }

  @Test
  void setPersonality_shouldClearMindWhenNull() {
    Actor actor = Actor.create("John", "Profile", "Description", "Personality");
    assertTrue(actor.getMind().isPresent());

    actor.setPersonality(null);

    assertTrue(actor.getMind().isEmpty());
  }

  @Test
  void setName_shouldUpdateName() {
    Actor actor = Actor.create("John", "Profile", "Description", null);
    actor.setName("Jane");
    assertEquals("Jane", actor.getName());
  }

  @Test
  void setName_shouldThrowWhenBlank() {
    Actor actor = Actor.create("John", "Profile", "Description", null);
    assertThrows(RuntimeException.class, () -> actor.setName(""));
  }

  @Test
  void setName_shouldThrowWhenNull() {
    Actor actor = Actor.create("John", "Profile", "Description", null);
    assertThrows(RuntimeException.class, () -> actor.setName(null));
  }

  @Test
  void setProfile_shouldUpdateProfile() {
    Actor actor = Actor.create("John", "Profile", "Description", null);
    actor.setProfile("New profile");
    assertEquals("New profile", actor.getProfile());
  }

  @Test
  void setProfile_shouldThrowWhenBlank() {
    Actor actor = Actor.create("John", "Profile", "Description", null);
    assertThrows(RuntimeException.class, () -> actor.setProfile(""));
  }

  @Test
  void setPhysicalDescription_shouldUpdateDescription() {
    Actor actor = Actor.create("John", "Profile", "Description", null);
    actor.setPhysicalDescription("Short with blonde hair");
    assertEquals("Short with blonde hair", actor.getPhysicalDescription());
  }

  @Test
  void setPhysicalDescription_shouldThrowWhenBlank() {
    Actor actor = Actor.create("John", "Profile", "Description", null);
    assertThrows(RuntimeException.class, () -> actor.setPhysicalDescription(""));
  }

  @Test
  void setUpdatedAt_shouldUpdateTimestamp() {
    Actor actor = Actor.create("John", "Profile", "Description", null);
    Instant newTime = actor.getUpdatedAt().plusSeconds(3600);
    actor.setUpdatedAt(newTime);
    assertEquals(newTime, actor.getUpdatedAt());
  }

  @Test
  void isValid_shouldReturnTrueForValidActor() {
    Actor actor = Actor.create("John", "Profile", "Description", "Personality");
    assertTrue(actor.isValid());
  }

  @Test
  void isValid_shouldReturnTrueWithNoMind() {
    Actor actor = Actor.create("John", "Profile", "Description", null);
    assertTrue(actor.isValid());
  }

  @Test
  void setPersonality_shouldThrowWhenSetToInvalidMind() {
    Actor actor = Actor.create("John", "Profile", "Description", null);
    actor.setPersonality("Valid personality");
    assertTrue(actor.getMind().isPresent());
  }

  @Test
  void isValid_shouldReturnFalseWhenCurrentOutfitIsNull() {
    Actor actor = Actor.create("John", "Profile", "Description", null);
    actor.chooseOutfit(null);
    assertTrue(actor.isValid());
  }
}