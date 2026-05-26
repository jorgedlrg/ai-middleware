package com.jorgedelarosa.aimiddleware.domain.scenario;

import static org.junit.jupiter.api.Assertions.*;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ScenarioDeletedEventTest {

  @Test
  void constructor_shouldSetAggregateIdAndVersion() {
    UUID scenarioId = UUID.randomUUID();
    AggregateRoot.AggregateId aggregateId = new AggregateRoot.AggregateId(Scenario.class, scenarioId);
    ScenarioDeletedEvent event = new ScenarioDeletedEvent(aggregateId, 1L);

    assertEquals(aggregateId, event.getAggregateId());
    assertEquals(1L, event.getVersion());
  }

  @Test
  void getSchemaVersion_shouldReturn1() {
    UUID scenarioId = UUID.randomUUID();
    AggregateRoot.AggregateId aggregateId = new AggregateRoot.AggregateId(Scenario.class, scenarioId);
    ScenarioDeletedEvent event = new ScenarioDeletedEvent(aggregateId, 1L);

    assertEquals(1, event.getSchemaVersion());
  }

  @Test
  void getOccurredAt_shouldBeSet() {
    UUID scenarioId = UUID.randomUUID();
    AggregateRoot.AggregateId aggregateId = new AggregateRoot.AggregateId(Scenario.class, scenarioId);
    ScenarioDeletedEvent event = new ScenarioDeletedEvent(aggregateId, 1L);

    assertNotNull(event.getOccurredAt());
  }
}