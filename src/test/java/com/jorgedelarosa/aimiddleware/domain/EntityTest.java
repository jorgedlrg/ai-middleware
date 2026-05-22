package com.jorgedelarosa.aimiddleware.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class EntityTest {

  @Test
  void constructor_shouldSetUuid() {
    UUID id = UUID.randomUUID();
    TestEntity entity = new TestEntity(id);
    assertEquals(id, entity.getId());
  }

  @Test
  void getId_shouldReturnUuid() {
    UUID id = UUID.randomUUID();
    TestEntity entity = new TestEntity(id);
    assertEquals(id, entity.getId());
  }

  @Test
  void isValid_shouldReturnTrueWhenUuidNotNull() {
    TestEntity entity = new TestEntity(UUID.randomUUID());
    assertTrue(entity.isValid());
  }

  @Test
  void isValid_shouldReturnFalseWhenUuidNull() {
    TestEntity entity = new TestEntity(null);
    assertFalse(entity.isValid());
  }

  @Test
  void validate_shouldNotThrowWhenValid() {
    TestEntity entity = new TestEntity(UUID.randomUUID());
    assertDoesNotThrow(entity::validate);
  }

  @Test
  void validate_shouldThrowWhenInvalid() {
    TestEntity entity = new TestEntity(null);
    RuntimeException ex = assertThrows(RuntimeException.class, entity::validate);
    assertTrue(ex.getMessage().contains("Entity"));
  }

  @Test
  void validate_shouldIncludeClassNameInException() {
    TestEntity entity = new TestEntity(null);
    RuntimeException ex = assertThrows(RuntimeException.class, entity::validate);
    assertTrue(ex.getMessage().contains("Entity"));
  }

  @Test
  void uuidCanBeAccessedAndUsedInComparisons() {
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    TestEntity entity1 = new TestEntity(id1);
    TestEntity entity2 = new TestEntity(id1);
    TestEntity entity3 = new TestEntity(id2);

    assertEquals(entity1.getId(), entity2.getId());
    assertNotEquals(entity1.getId(), entity3.getId());
  }

  private static class TestEntity extends Entity {
    private TestEntity(UUID id) {
      super(id);
    }
  }
}