package com.jorgedelarosa.aimiddleware.domain.scenario;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class IntroductionTest {

  @Test
  void create_shouldGenerateIdAndSetFields() {
    Context context = Context.create("Living Room", "A cozy space");
    Role performer = Role.create("Hero", "Main character");
    Introduction intro = Introduction.create(
        "Hello, world!",
        Optional.of("What should I say?"),
        Optional.of("Enter the room"),
        performer,
        context);

    assertNotNull(intro.getId());
    assertEquals("Hello, world!", intro.getSpokenText());
    assertTrue(intro.getThoughtText().isPresent());
    assertEquals("What should I say?", intro.getThoughtText().get());
    assertTrue(intro.getActionText().isPresent());
    assertEquals("Enter the room", intro.getActionText().get());
    assertEquals(performer, intro.getPerformer());
    assertEquals(context, intro.getContext());
  }

  @Test
  void create_shouldAllowEmptyOptionals() {
    Context context = Context.create("Room", "Description");
    Role role = Role.create("Role", "Details");
    Introduction intro = Introduction.create(
        "Speech only",
        Optional.empty(),
        Optional.empty(),
        role,
        context);

    assertEquals("Speech only", intro.getSpokenText());
    assertTrue(intro.getThoughtText().isEmpty());
    assertTrue(intro.getActionText().isEmpty());
  }

  @Test
  void restore_shouldUseProvidedIdAndFields() {
    UUID id = UUID.randomUUID();
    Context context = Context.create("Room", "Description");
    Role role = Role.create("Role", "Details");
    Introduction intro = Introduction.restore(
        id,
        "Restored speech",
        Optional.of("Thinking"),
        Optional.of("Action"),
        role,
        context);

    assertEquals(id, intro.getId());
    assertEquals("Restored speech", intro.getSpokenText());
  }

  @Test
  void setSpokenText_shouldUpdateText() {
    Context context = Context.create("Room", "Description");
    Role role = Role.create("Role", "Details");
    Introduction intro = Introduction.create("Original", Optional.empty(), Optional.empty(), role, context);

    intro.setSpokenText("Updated");

    assertEquals("Updated", intro.getSpokenText());
  }

@Test
  void setSpokenText_shouldThrowWhenBlank() {
    Context context = Context.create("Room", "Description");
    Role role = Role.create("Role", "Details");
    Introduction intro = Introduction.create("Original", Optional.empty(), Optional.empty(), role, context);

    assertThrows(RuntimeException.class, () -> intro.setSpokenText(""));
  }

  @Test
  void setSpokenText_shouldThrowWhenNull() {
    Context context = Context.create("Room", "Description");
    Role role = Role.create("Role", "Details");
    Introduction intro = Introduction.create("Original", Optional.empty(), Optional.empty(), role, context);

    assertThrows(RuntimeException.class, () -> intro.setSpokenText(null));
  }

  @Test
  void setThoughtText_shouldUpdateThought() {
    Context context = Context.create("Room", "Description");
    Role role = Role.create("Role", "Details");
    Introduction intro = Introduction.create("Speech", Optional.empty(), Optional.empty(), role, context);

    intro.setThoughtText(Optional.of("New thought"));

    assertTrue(intro.getThoughtText().isPresent());
    assertEquals("New thought", intro.getThoughtText().get());
  }

  @Test
  void setThoughtText_shouldAllowEmpty() {
    Context context = Context.create("Room", "Description");
    Role role = Role.create("Role", "Details");
    Introduction intro = Introduction.create("Speech", Optional.of("Old"), Optional.empty(), role, context);

    intro.setThoughtText(Optional.empty());

    assertTrue(intro.getThoughtText().isEmpty());
  }

  @Test
  void setActionText_shouldUpdateAction() {
    Context context = Context.create("Room", "Description");
    Role role = Role.create("Role", "Details");
    Introduction intro = Introduction.create("Speech", Optional.empty(), Optional.empty(), role, context);

    intro.setActionText(Optional.of("New action"));

    assertTrue(intro.getActionText().isPresent());
    assertEquals("New action", intro.getActionText().get());
  }

  @Test
  void setActionText_shouldAllowEmpty() {
    Context context = Context.create("Room", "Description");
    Role role = Role.create("Role", "Details");
    Introduction intro = Introduction.create("Speech", Optional.empty(), Optional.of("Old"), role, context);

    intro.setActionText(Optional.empty());

    assertTrue(intro.getActionText().isEmpty());
  }

  @Test
  void isValid_shouldReturnTrueForValidIntroduction() {
    Context context = Context.create("Room", "Description");
    Role role = Role.create("Role", "Details");
    Introduction intro = Introduction.create("Valid speech", Optional.empty(), Optional.empty(), role, context);

    assertTrue(intro.isValid());
  }
}