package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import java.util.List;

/**
 * @author jorge
 */
public interface GetScenariosOutPort {

  public List<Scenario> query();
}
