package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "outfit")
@Data
@EqualsAndHashCode(callSuper = false)
public class OutfitEntity extends BaseJpaEntity {

  @Id private UUID id;
  private String name;
  private String description;
}
