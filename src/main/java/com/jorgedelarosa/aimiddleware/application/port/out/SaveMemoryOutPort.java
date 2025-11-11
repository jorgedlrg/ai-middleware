package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.actor.Memory;

/**
 * @author jorge
 */
public interface SaveMemoryOutPort {

  public void save(Memory memory);
}
