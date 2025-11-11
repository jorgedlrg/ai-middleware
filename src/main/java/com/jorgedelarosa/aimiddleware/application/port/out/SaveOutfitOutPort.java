package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.actor.Outfit;

/**
 * @author jorge
 */
public interface SaveOutfitOutPort {
  public void save(Outfit outfit);
}
