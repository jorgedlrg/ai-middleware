package com.jorgedelarosa.aimiddleware.domain.scenario;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ContextTest {

  @Test
  void create_shouldGenerateIdAndSetFields() {
    Context context = Context.create("Living Room", "A cozy space with wooden furniture");

    assertNotNull(context.getId());
    assertEquals("Living Room", context.getName());
    assertEquals("A cozy space with wooden furniture", context.getPhysicalDescription());
  }

  @Test
  void restore_shouldUseProvidedIdAndFields() {
    UUID id = UUID.randomUUID();
    Context context = Context.restore(id, "Kitchen", "Modern with marble countertops");

    assertEquals(id, context.getId());
    assertEquals("Kitchen", context.getName());
    assertEquals("Modern with marble countertops", context.getPhysicalDescription());
  }

  @Test
  void setName_shouldUpdateName() {
    Context context = Context.create("Old Name", "Description");
    context.setName("New Name");

    assertEquals("New Name", context.getName());
  }

  @Test
  void setName_shouldThrowWhenBlank() {
    Context context = Context.create("Name", "Description");
    assertThrows(RuntimeException.class, () -> context.setName(""));
  }

  @Test
  void setName_shouldThrowWhenNull() {
    Context context = Context.create("Name", "Description");
    assertThrows(RuntimeException.class, () -> context.setName(null));
  }

  @Test
  void setPhysicalDescription_shouldUpdateDescription() {
    Context context = Context.create("Room", "Old description");
    context.setPhysicalDescription("New description");

    assertEquals("New description", context.getPhysicalDescription());
  }

  @Test
  void setPhysicalDescription_shouldThrowWhenBlank() {
    Context context = Context.create("Room", "Description");
    assertThrows(RuntimeException.class, () -> context.setPhysicalDescription(""));
  }

  @Test
  void setPhysicalDescription_shouldThrowWhenNull() {
    Context context = Context.create("Room", "Description");
    assertThrows(RuntimeException.class, () -> context.setPhysicalDescription(null));
  }

  @Test
  void isValid_shouldReturnTrueForValidContext() {
    Context context = Context.create("Room", "Description");
    assertTrue(context.isValid());
  }
}