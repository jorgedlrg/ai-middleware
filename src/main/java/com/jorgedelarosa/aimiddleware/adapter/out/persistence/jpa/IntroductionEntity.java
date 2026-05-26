package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "introduction")
@Data
@EqualsAndHashCode(callSuper = false)
public class IntroductionEntity extends BaseJpaEntity {
  @Id private UUID id;

  private UUID scenario;

  @Column(name = "spoken_text")
  private String spokenText;

  @Column(name = "thought_text")
  private String thoughtText;

  @Column(name = "action_text")
  private String actionText;

  @Column(name = "role")
  private UUID performer;

  private UUID context;
}
