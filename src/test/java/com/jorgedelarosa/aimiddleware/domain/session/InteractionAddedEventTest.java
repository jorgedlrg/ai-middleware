package com.jorgedelarosa.aimiddleware.domain.session;

import static org.junit.jupiter.api.Assertions.*;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class InteractionAddedEventTest {

  @Test
  void constructor_shouldSetAggregateIdVersionAndAutoreplyRole() {
    UUID sessionId = UUID.randomUUID();
    UUID roleId = UUID.randomUUID();
    AggregateRoot.AggregateId aggregateId = new AggregateRoot.AggregateId(Session.class, sessionId);
    Interaction interaction = Interaction.create(
        Optional.empty(),
        new InteractionText("Speech", Optional.empty()),
        Optional.empty(),
        roleId,
        UUID.randomUUID(),
        UUID.randomUUID(),
        Optional.empty(),
        Optional.empty());

    InteractionAddedEvent event = new InteractionAddedEvent(aggregateId, 1L, interaction, Optional.of(roleId));

    assertEquals(aggregateId, event.getAggregateId());
    assertEquals(1L, event.getVersion());
    assertTrue(event.getAutoreplyRole().isPresent());
    assertEquals(roleId, event.getAutoreplyRole().get());
  }

  @Test
  void constructor_shouldAllowEmptyAutoreplyRole() {
    UUID sessionId = UUID.randomUUID();
    UUID roleId = UUID.randomUUID();
    AggregateRoot.AggregateId aggregateId = new AggregateRoot.AggregateId(Session.class, sessionId);
    Interaction interaction = Interaction.create(
        Optional.empty(),
        new InteractionText("Speech", Optional.empty()),
        Optional.empty(),
        roleId,
        UUID.randomUUID(),
        UUID.randomUUID(),
        Optional.empty(),
        Optional.empty());

    InteractionAddedEvent event = new InteractionAddedEvent(aggregateId, 1L, interaction, Optional.empty());

    assertTrue(event.getAutoreplyRole().isEmpty());
  }

  @Test
  void getSchemaVersion_shouldReturn1() {
    UUID sessionId = UUID.randomUUID();
    UUID roleId = UUID.randomUUID();
    AggregateRoot.AggregateId aggregateId = new AggregateRoot.AggregateId(Session.class, sessionId);
    Interaction interaction = Interaction.create(
        Optional.empty(),
        new InteractionText("Speech", Optional.empty()),
        Optional.empty(),
        roleId,
        UUID.randomUUID(),
        UUID.randomUUID(),
        Optional.empty(),
        Optional.empty());

    InteractionAddedEvent event = new InteractionAddedEvent(aggregateId, 1L, interaction, Optional.empty());

    assertEquals(1, event.getSchemaVersion());
  }

  @Test
  void getOccurredAt_shouldBeSet() {
    UUID sessionId = UUID.randomUUID();
    UUID roleId = UUID.randomUUID();
    AggregateRoot.AggregateId aggregateId = new AggregateRoot.AggregateId(Session.class, sessionId);
    Interaction interaction = Interaction.create(
        Optional.empty(),
        new InteractionText("Speech", Optional.empty()),
        Optional.empty(),
        roleId,
        UUID.randomUUID(),
        UUID.randomUUID(),
        Optional.empty(),
        Optional.empty());

    InteractionAddedEvent event = new InteractionAddedEvent(aggregateId, 1L, interaction, Optional.empty());

    assertNotNull(event.getOccurredAt());
  }
}