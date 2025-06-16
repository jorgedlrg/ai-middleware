package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;

/**
 * @author jorge
 */
@Entity(name = "actor")
@Data
public class ActorEntity {
  @Id private UUID id;
  private String name;

  @Column(name = "physical_desc")
  private String physicalDescription;
  
  @Column(name = "current_outfit")
  private UUID currentOutfit;
}
