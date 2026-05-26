package com.jorgedelarosa.aimiddleware.domain.actor;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MemoryTest {

  @Test
  void create_shouldGenerateIdAndSetActor() {
    UUID actorId = UUID.randomUUID();
    Memory memory = Memory.create(actorId);

    assertNotNull(memory.getId());
    assertEquals(actorId, memory.getActor());
    assertTrue(memory.getFragments().isEmpty());
  }

  @Test
  void create_shouldGenerateNewUuidForEachInstance() {
    UUID actorId = UUID.randomUUID();
    Memory memory1 = Memory.create(actorId);
    Memory memory2 = Memory.create(actorId);

    assertNotEquals(memory1.getId(), memory2.getId());
  }

  @Test
  void restore_shouldUseProvidedIdAndFragments() {
    UUID actorId = UUID.randomUUID();
    UUID memoryId = UUID.randomUUID();
    List<MemoryFragment> fragments = new ArrayList<>();
    fragments.add(MemoryFragment.create("First memory"));
    fragments.add(MemoryFragment.create("Second memory"));

    Memory memory = Memory.restore(actorId, fragments, memoryId);

    assertEquals(memoryId, memory.getId());
    assertEquals(actorId, memory.getActor());
    assertEquals(2, memory.getFragments().size());
  }

  @Test
  void restore_shouldCreateCopyOfFragments() {
    UUID actorId = UUID.randomUUID();
    UUID memoryId = UUID.randomUUID();
    List<MemoryFragment> fragments = new ArrayList<>();
    fragments.add(MemoryFragment.create("Original"));

    Memory memory = Memory.restore(actorId, fragments, memoryId);

    fragments.add(MemoryFragment.create("Modified"));
    assertEquals(1, memory.getFragments().size());
  }

  @Test
  void addFragment_shouldAddFragmentToList() {
    Memory memory = Memory.create(UUID.randomUUID());
    assertTrue(memory.getFragments().isEmpty());

    memory.addFragment("First fragment");
    memory.addFragment("Second fragment");

    assertEquals(2, memory.getFragments().size());
    assertEquals("First fragment", memory.getFragments().get(0).getText());
    assertEquals("Second fragment", memory.getFragments().get(1).getText());
  }

  @Test
  void addFragment_shouldCreateFragmentWithCurrentTimestamp() {
    Memory memory = Memory.create(UUID.randomUUID());
    memory.addFragment("Test fragment");

    assertNotNull(memory.getFragments().get(0).getTimestamp());
  }

  @Test
  void deleteFragment_shouldRemoveFragmentById() {
    Memory memory = Memory.create(UUID.randomUUID());
    memory.addFragment("To be deleted");
    UUID fragmentId = memory.getFragments().get(0).getId();

    memory.deleteFragment(fragmentId);

    assertTrue(memory.getFragments().isEmpty());
  }

  @Test
  void deleteFragment_shouldNotModifyListWhenIdNotFound() {
    Memory memory = Memory.create(UUID.randomUUID());
    memory.addFragment("Preserved fragment");

    memory.deleteFragment(UUID.randomUUID());

    assertEquals(1, memory.getFragments().size());
    assertEquals("Preserved fragment", memory.getFragments().get(0).getText());
  }

  @Test
  void getFragments_shouldReturnUnmodifiableList() {
    Memory memory = Memory.create(UUID.randomUUID());
    memory.addFragment("Test");

    assertThrows(UnsupportedOperationException.class, () ->
        memory.getFragments().add(MemoryFragment.create("Should fail")));
  }

  @Test
  void isValid_shouldReturnTrueForValidMemory() {
    Memory memory = Memory.create(UUID.randomUUID());
    assertTrue(memory.isValid());
  }

  @Test
  void restore_shouldThrowWhenActorIsNull() {
    assertThrows(RuntimeException.class, () ->
        Memory.restore(null, new ArrayList<>(), UUID.randomUUID()));
  }

  @Test
  void restore_shouldThrowWhenFragmentsIsNull() {
    assertThrows(RuntimeException.class, () ->
        Memory.restore(UUID.randomUUID(), null, UUID.randomUUID()));
  }
}