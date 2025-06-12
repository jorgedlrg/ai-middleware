package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

/**
 * @author jorge
 */
@Embeddable
@Data
public class PerformanceId implements Serializable {
  private UUID session;
  private UUID role;
}
