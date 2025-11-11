package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.actor.Outfit;
import java.util.Optional;
import java.util.UUID;

/**
 * @author jorge
 */
public interface GetOutfitByIdOutPort {

  public Optional<Outfit> query(UUID id);
}
