package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;

/**
 * @author jorge
 */
@Entity(name = "outfit")
@Data
public class OutfitEntity {

  @Id private UUID id;
  private UUID actor;
  private String description;
}
