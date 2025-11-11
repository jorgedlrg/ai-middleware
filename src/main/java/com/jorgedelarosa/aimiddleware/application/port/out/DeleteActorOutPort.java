package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.actor.Actor;

/**
 * @author jorge
 */
public interface DeleteActorOutPort {
  public void delete(Actor actor);
}
