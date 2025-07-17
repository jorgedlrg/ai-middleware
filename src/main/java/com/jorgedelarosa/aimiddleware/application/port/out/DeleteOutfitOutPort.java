package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.actor.Outfit;

/**
 * @author jorge
 */
public interface DeleteOutfitOutPort {
  public void delete(Outfit outfit);
}
