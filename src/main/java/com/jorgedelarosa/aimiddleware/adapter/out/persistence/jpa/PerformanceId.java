package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jorge
 */
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceId implements Serializable {
  private UUID session;
  private UUID role;
}
