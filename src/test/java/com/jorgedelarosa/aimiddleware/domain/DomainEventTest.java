package com.jorgedelarosa.aimiddleware.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DomainEventTest {

  @Test
  void constructor_shouldGenerateEventId() {
    TestDomainEvent event = new TestDomainEvent(
        new AggregateRoot.AggregateId(TestAggregateRoot.class, UUID.randomUUID()), 1L);
    assertNotNull(event.getEventId());
  }

  @Test
  void constructor_shouldSetOccurredAtToNow() {
    Instant before = Instant.now();
    TestDomainEvent event = new TestDomainEvent(
        new AggregateRoot.AggregateId(TestAggregateRoot.class, UUID.randomUUID()), 1L);
    Instant after = Instant.now();

    assertNotNull(event.getOccurredAt());
    assertTrue(event.getOccurredAt().compareTo(before) >= 0);
    assertTrue(event.getOccurredAt().compareTo(after) <= 0);
  }

  @Test
  void constructor_shouldSetAggregateId() {
    UUID id = UUID.randomUUID();
    AggregateRoot.AggregateId aggregateId = new AggregateRoot.AggregateId(TestAggregateRoot.class, id);
    TestDomainEvent event = new TestDomainEvent(aggregateId, 1L);

    assertEquals(aggregateId, event.getAggregateId());
    assertEquals(TestAggregateRoot.class, event.getAggregateId().getClazz());
    assertEquals(id, event.getAggregateId().getId());
  }

  @Test
  void constructor_shouldSetVersion() {
    TestDomainEvent event = new TestDomainEvent(
        new AggregateRoot.AggregateId(TestAggregateRoot.class, UUID.randomUUID()), 42L);
    assertEquals(42L, event.getVersion());
  }

  @Test
  void getEventId_shouldReturnUniqueIds() {
    TestDomainEvent event1 = new TestDomainEvent(
        new AggregateRoot.AggregateId(TestAggregateRoot.class, UUID.randomUUID()), 1L);
    TestDomainEvent event2 = new TestDomainEvent(
        new AggregateRoot.AggregateId(TestAggregateRoot.class, UUID.randomUUID()), 1L);

    assertNotEquals(event1.getEventId(), event2.getEventId());
  }

  @Test
  void getSchemaVersion_shouldReturnImplementedValue() {
    TestDomainEvent event = new TestDomainEvent(
        new AggregateRoot.AggregateId(TestAggregateRoot.class, UUID.randomUUID()), 1L);
    assertEquals(1, event.getSchemaVersion());
  }

  private static class TestAggregateRoot extends AggregateRoot {
    private TestAggregateRoot(UUID id) {
      super(TestAggregateRoot.class, id);
    }
  }

  private static class TestDomainEvent extends DomainEvent {
    private TestDomainEvent(AggregateRoot.AggregateId aggregateId, Long version) {
      super(aggregateId, version);
    }

    @Override
    public int getSchemaVersion() {
      return 1;
    }
  }
}