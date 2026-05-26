package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "context")
@Data
@EqualsAndHashCode(callSuper = false)
public class ContextEntity extends BaseJpaEntity {
  @Id private UUID id;
  private UUID scenario;
  private String name;

  @Column(name = "physical_desc")
  private String physicalDescription;
}
