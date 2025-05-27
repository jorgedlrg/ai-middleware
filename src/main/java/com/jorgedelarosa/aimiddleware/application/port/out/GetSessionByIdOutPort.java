package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.Optional;
import java.util.UUID;

/**
 * @author jorge
 */
public interface GetSessionByIdOutPort {

  public Optional<Session> query(UUID id);
}
