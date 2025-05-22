package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.UUID;

/**
 * @author jorge
 */
public interface GetSessionByIdOutPort {

  public Session query(UUID id);
}
