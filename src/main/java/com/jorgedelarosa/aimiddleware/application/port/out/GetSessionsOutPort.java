package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.List;

/**
 * @author jorge
 */
public interface GetSessionsOutPort {
  public List<Session> query();
}
