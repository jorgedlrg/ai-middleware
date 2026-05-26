package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "role")
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleEntity extends BaseJpaEntity {
  @Id private UUID id;
  private UUID scenario;
  private String name;
  private String details;
}
