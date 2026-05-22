package com.jorgedelarosa.aimiddleware.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class AggregateRootTest {

  @Test
  void constructor_shouldSetUuidAndAggregateId() {
    UUID id = UUID.randomUUID();
    TestAggregateRoot aggregate = new TestAggregateRoot(id);
    assertEquals(id, aggregate.getId());
    assertNotNull(aggregate.getAggregateId());
  }

  @Test
  void getAggregateId_shouldReturnAggregateIdWithCorrectClass() {
    UUID id = UUID.randomUUID();
    TestAggregateRoot aggregate = new TestAggregateRoot(id);
    assertEquals(TestAggregateRoot.class, aggregate.getAggregateId().getClazz());
  }

  @Test
  void getAggregateId_shouldReturnAggregateIdWithCorrectUuid() {
    UUID id = UUID.randomUUID();
    TestAggregateRoot aggregate = new TestAggregateRoot(id);
    assertEquals(id, aggregate.getAggregateId().getId());
  }

  @Test
  void aggregateId_toString_shouldReturnUrnFormat() {
    UUID id = UUID.randomUUID();
    TestAggregateRoot aggregate = new TestAggregateRoot(id);
    String urn = aggregate.getAggregateId().toString();

    assertTrue(urn.startsWith("urn:"));
    assertTrue(urn.contains("TestAggregateRoot"));
    assertTrue(urn.contains(id.toString()));
  }

  @Test
  void aggregateId_getClazz_shouldReturnStoredClass() {
    UUID id = UUID.randomUUID();
    TestAggregateRoot aggregate = new TestAggregateRoot(id);
    assertEquals(TestAggregateRoot.class, aggregate.getAggregateId().getClazz());
  }

  @Test
  void aggregateId_getId_shouldReturnStoredUuid() {
    UUID id = UUID.randomUUID();
    TestAggregateRoot aggregate = new TestAggregateRoot(id);
    assertEquals(id, aggregate.getAggregateId().getId());
  }

  @Test
  void isValid_shouldBeTrueWhenUuidNotNull() {
    TestAggregateRoot aggregate = new TestAggregateRoot(UUID.randomUUID());
    assertTrue(aggregate.isValid());
  }

  @Test
  void isValid_shouldBeFalseWhenUuidNull() {
    TestAggregateRoot aggregate = new TestAggregateRoot(null);
    assertFalse(aggregate.isValid());
  }

  private static class TestAggregateRoot extends AggregateRoot {
    private TestAggregateRoot(UUID id) {
      super(TestAggregateRoot.class, id);
    }
  }
}