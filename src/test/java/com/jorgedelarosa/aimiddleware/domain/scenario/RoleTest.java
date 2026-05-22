package com.jorgedelarosa.aimiddleware.domain.scenario;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class RoleTest {

  @Test
  void create_shouldGenerateIdAndSetFields() {
    Role role = Role.create("Hero", "The main protagonist of the story");

    assertNotNull(role.getId());
    assertEquals("Hero", role.getName());
    assertEquals("The main protagonist of the story", role.getDetails());
  }

  @Test
  void restore_shouldUseProvidedIdAndFields() {
    UUID id = UUID.randomUUID();
    Role role = Role.restore(id, "Villain", "The antagonist");

    assertEquals(id, role.getId());
    assertEquals("Villain", role.getName());
    assertEquals("The antagonist", role.getDetails());
  }

  @Test
  void setName_shouldUpdateName() {
    Role role = Role.create("Old Name", "Details");
    role.setName("New Name");

    assertEquals("New Name", role.getName());
  }

  @Test
  void setName_shouldThrowWhenBlank() {
    Role role = Role.create("Name", "Details");
    assertThrows(RuntimeException.class, () -> role.setName(""));
  }

  @Test
  void setName_shouldThrowWhenNull() {
    Role role = Role.create("Name", "Details");
    assertThrows(RuntimeException.class, () -> role.setName(null));
  }

  @Test
  void setDetails_shouldUpdateDetails() {
    Role role = Role.create("Role", "Old details");
    role.setDetails("New details");

    assertEquals("New details", role.getDetails());
  }

  @Test
  void setDetails_shouldThrowWhenBlank() {
    Role role = Role.create("Role", "Details");
    assertThrows(RuntimeException.class, () -> role.setDetails(""));
  }

  @Test
  void setDetails_shouldThrowWhenNull() {
    Role role = Role.create("Role", "Details");
    assertThrows(RuntimeException.class, () -> role.setDetails(null));
  }

  @Test
  void isValid_shouldReturnTrueForValidRole() {
    Role role = Role.create("Name", "Details");
    assertTrue(role.isValid());
  }
}