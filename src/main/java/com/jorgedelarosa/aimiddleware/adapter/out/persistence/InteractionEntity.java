package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;

/**
 * @author jorge
 */
@Entity(name = "interaction")
@Data
public class InteractionEntity {
  @Id private UUID id;
  private UUID role;
  private UUID session;
  private long timestamp;
  private String text;
}
