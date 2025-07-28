package com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;

/**
 * @author jorge
 */
@Entity(name = "introduction")
@Data
public class IntroductionEntity {
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
