package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;

/**
 * @author jorge
 */
@Entity(name = "session")
@Data
public class SessionEntity {

   @Id private UUID id;
   private UUID scenario;
}
