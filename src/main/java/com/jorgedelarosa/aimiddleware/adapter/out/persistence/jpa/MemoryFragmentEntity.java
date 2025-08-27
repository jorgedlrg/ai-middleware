package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;

/**
 * @author jorge
 */
@Entity(name = "memory_fragment")
@Data
public class MemoryFragmentEntity {

  @Id private UUID id;
  private UUID owner;
  private long timestamp;
  private String text;
}
