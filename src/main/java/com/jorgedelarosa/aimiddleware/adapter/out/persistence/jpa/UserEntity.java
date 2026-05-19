package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "usertable")
@Data
@EqualsAndHashCode(callSuper = false)
public class UserEntity extends BaseJpaEntity {

  @Id private UUID id;
  private String email;
}
