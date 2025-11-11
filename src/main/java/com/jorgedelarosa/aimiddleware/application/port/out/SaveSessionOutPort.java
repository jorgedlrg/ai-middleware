package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.session.Session;

/**
 * @author jorge
 */
public interface SaveSessionOutPort {

  public void save(Session session);
}
