package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;

/**
 * @author jorge
 */
@Entity
@Data
public class ScenarioEntity {
  @Id private UUID id;
}
