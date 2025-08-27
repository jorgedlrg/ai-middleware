package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.actor.Memory;
import java.util.UUID;

/**
 *
 * @author jorge
 */
public interface GetMemoryByActorOutPort {

  public Memory query(UUID actor);
  
}
