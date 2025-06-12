package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import java.util.List;
import java.util.UUID;

/**
 * @author jorge
 */
public interface GetActorListByIdOutPort {

  public List<Actor> query(List<UUID> ids);
}
