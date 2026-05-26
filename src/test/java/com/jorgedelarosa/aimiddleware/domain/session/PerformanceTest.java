package com.jorgedelarosa.aimiddleware.domain.session;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class PerformanceTest {

  @Test
  void constructor_shouldSetActorAndRole() {
    UUID actorId = UUID.randomUUID();
    UUID roleId = UUID.randomUUID();
    Performance performance = new Performance(actorId, roleId);

    assertEquals(actorId, performance.getActor());
    assertEquals(roleId, performance.getRole());
  }

  @Test
  void getActor_shouldReturnActorId() {
    UUID actorId = UUID.randomUUID();
    UUID roleId = UUID.randomUUID();
    Performance performance = new Performance(actorId, roleId);

    assertEquals(actorId, performance.getActor());
  }

  @Test
  void getRole_shouldReturnRoleId() {
    UUID actorId = UUID.randomUUID();
    UUID roleId = UUID.randomUUID();
    Performance performance = new Performance(actorId, roleId);

    assertEquals(roleId, performance.getRole());
  }

  @Test
  void constructor_shouldThrowWhenActorIsNull() {
    UUID roleId = UUID.randomUUID();
    assertThrows(RuntimeException.class, () -> new Performance(null, roleId));
  }

  @Test
  void constructor_shouldThrowWhenRoleIsNull() {
    UUID actorId = UUID.randomUUID();
    assertThrows(RuntimeException.class, () -> new Performance(actorId, null));
  }

  @Test
  void constructor_shouldThrowWhenBothAreNull() {
    assertThrows(RuntimeException.class, () -> new Performance(null, null));
  }
}