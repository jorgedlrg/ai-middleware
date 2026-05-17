package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "performance")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceEntity extends BaseJpaEntity {
  @EmbeddedId private PerformanceId performanceId;
  private UUID actor;
}
