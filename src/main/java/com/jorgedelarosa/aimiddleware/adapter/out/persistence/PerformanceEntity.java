package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import java.util.UUID;
import lombok.Data;

/**
 * @author jorge
 */
@Entity(name = "performance")
@Data
public class PerformanceEntity {
  @EmbeddedId private PerformanceId performanceId;
  private UUID actor;
}
