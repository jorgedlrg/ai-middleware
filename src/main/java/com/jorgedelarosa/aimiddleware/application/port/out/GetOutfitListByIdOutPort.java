package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.actor.Outfit;
import java.util.List;
import java.util.UUID;

/**
 * @author jorge
 */
public interface GetOutfitListByIdOutPort {

  public List<Outfit> query(List<UUID> ids);
}
