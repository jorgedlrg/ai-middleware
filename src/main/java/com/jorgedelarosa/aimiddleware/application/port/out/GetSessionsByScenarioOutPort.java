package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.List;
import java.util.UUID;

/**
 * @author jorge
 */
public interface GetSessionsByScenarioOutPort {

  public List<Session> queryByScenario(UUID scenario);
}
