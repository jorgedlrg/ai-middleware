package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;

/**
 * @author jorge
 */
public interface SaveScenarioOutPort {
  public void save(Scenario scenario);
}
