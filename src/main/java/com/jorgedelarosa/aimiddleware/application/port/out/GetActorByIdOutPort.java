package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.Actor;
import java.util.Optional;
import java.util.UUID;

/**
 * @author jorge
 */
public interface GetActorByIdOutPort {

  public Optional<Actor> query(UUID id);
}
