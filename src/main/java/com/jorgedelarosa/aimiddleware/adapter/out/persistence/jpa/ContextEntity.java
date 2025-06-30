package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;

/**
 * @author jorge
 */
@Entity(name = "context")
@Data
public class ContextEntity {
  @Id private UUID id;
  private UUID scenario;
  private String name;

  @Column(name = "physical_desc")
  private String physicalDescription;
}
