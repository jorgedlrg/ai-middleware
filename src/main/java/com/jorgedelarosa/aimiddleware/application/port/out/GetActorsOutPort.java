package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import java.util.List;

/**
 * @author jorge
 */
public interface GetActorsOutPort {

  public List<Actor> query();
}
