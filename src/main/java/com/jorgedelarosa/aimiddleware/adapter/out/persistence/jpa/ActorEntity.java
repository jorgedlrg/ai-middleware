package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "actor")
@Data
@EqualsAndHashCode(callSuper = false)
public class ActorEntity extends BaseJpaEntity {
  @Id private UUID id;
  private String name;

  private String profile;

  @Column(name = "physical_desc")
  private String physicalDescription;

  @Column(name = "current_outfit")
  private UUID currentOutfit;
}
