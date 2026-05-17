package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;

@Entity(name = "usertable")
@Data
public class UserEntity extends BaseJpaEntity {

  @Id private UUID id;
  private String email;
}
