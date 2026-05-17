package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;

@Entity(name = "mind")
@Data
public class MindEntity extends BaseJpaEntity {

  @Id private UUID actor;
  private String personality;
}
