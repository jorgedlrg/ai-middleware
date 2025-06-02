package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;

/**
 * @author jorge
 */
@Entity(name = "interaction")
@Data
public class InteractionEntity {
  @Id private UUID id;
  private UUID role;
  private UUID session;
  private long timestamp;
  private String text;

  @Column(name = "isuser")
  private boolean
      user; // FIXME: remove this. I'm using this in an initial stage to know which interactions are
  // made by the user so the AI clients work.
}
