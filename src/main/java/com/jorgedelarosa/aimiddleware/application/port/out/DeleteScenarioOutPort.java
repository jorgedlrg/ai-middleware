package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;

/**
 * @author jorge
 */
public interface DeleteScenarioOutPort {
  public void delete(Scenario scenario);
}
