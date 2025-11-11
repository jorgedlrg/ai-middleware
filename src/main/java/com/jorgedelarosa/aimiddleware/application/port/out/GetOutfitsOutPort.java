package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.actor.Outfit;
import java.util.List;

/**
 * @author jorge
 */
public interface GetOutfitsOutPort {
  public List<Outfit> query();
}
