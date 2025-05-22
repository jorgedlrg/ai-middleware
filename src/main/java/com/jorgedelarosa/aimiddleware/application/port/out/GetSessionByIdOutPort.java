package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.session.Session;


/**
 * @author jorge
 */
public interface GetSessionByIdOutPort {

  public Session query();
}
