package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.actor.Actor;

/**
 * @author jorge
 */
public interface SaveActorOutPort {

  public void save(Actor actor);
}
