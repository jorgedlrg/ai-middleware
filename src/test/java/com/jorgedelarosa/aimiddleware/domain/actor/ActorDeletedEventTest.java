package com.jorgedelarosa.aimiddleware.domain.actor;

import static org.junit.jupiter.api.Assertions.*;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ActorDeletedEventTest {

  @Test
  void constructor_shouldSetAggregateIdAndVersion() {
    UUID actorId = UUID.randomUUID();
    AggregateRoot.AggregateId aggregateId = new AggregateRoot.AggregateId(Actor.class, actorId);
    ActorDeletedEvent event = new ActorDeletedEvent(aggregateId, 1L);

    assertEquals(aggregateId, event.getAggregateId());
    assertEquals(1L, event.getVersion());
  }

  @Test
  void getSchemaVersion_shouldReturn1() {
    UUID actorId = UUID.randomUUID();
    AggregateRoot.AggregateId aggregateId = new AggregateRoot.AggregateId(Actor.class, actorId);
    ActorDeletedEvent event = new ActorDeletedEvent(aggregateId, 1L);

    assertEquals(1, event.getSchemaVersion());
  }

  @Test
  void getEventId_shouldBeUnique() {
    UUID actorId = UUID.randomUUID();
    AggregateRoot.AggregateId aggregateId = new AggregateRoot.AggregateId(Actor.class, actorId);
    ActorDeletedEvent event1 = new ActorDeletedEvent(aggregateId, 1L);
    ActorDeletedEvent event2 = new ActorDeletedEvent(aggregateId, 1L);

    assertNotEquals(event1.getEventId(), event2.getEventId());
  }
}