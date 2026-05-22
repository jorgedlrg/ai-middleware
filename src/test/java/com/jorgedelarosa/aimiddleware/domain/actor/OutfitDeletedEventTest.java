package com.jorgedelarosa.aimiddleware.domain.actor;

import static org.junit.jupiter.api.Assertions.*;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OutfitDeletedEventTest {

  @Test
  void constructor_shouldSetAggregateIdAndVersion() {
    UUID outfitId = UUID.randomUUID();
    AggregateRoot.AggregateId aggregateId = new AggregateRoot.AggregateId(Outfit.class, outfitId);
    OutfitDeletedEvent event = new OutfitDeletedEvent(aggregateId, 1L);

    assertEquals(aggregateId, event.getAggregateId());
    assertEquals(1L, event.getVersion());
  }

  @Test
  void getSchemaVersion_shouldReturn1() {
    UUID outfitId = UUID.randomUUID();
    AggregateRoot.AggregateId aggregateId = new AggregateRoot.AggregateId(Outfit.class, outfitId);
    OutfitDeletedEvent event = new OutfitDeletedEvent(aggregateId, 1L);

    assertEquals(1, event.getSchemaVersion());
  }

  @Test
  void getOccurredAt_shouldBeSet() {
    UUID outfitId = UUID.randomUUID();
    AggregateRoot.AggregateId aggregateId = new AggregateRoot.AggregateId(Outfit.class, outfitId);
    OutfitDeletedEvent event = new OutfitDeletedEvent(aggregateId, 1L);

    assertNotNull(event.getOccurredAt());
  }
}