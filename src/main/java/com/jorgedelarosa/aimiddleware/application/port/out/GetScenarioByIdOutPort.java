package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import java.util.UUID;

/**
 * @author jorge
 */
public interface GetScenarioByIdOutPort {

 public Scenario query(UUID id);
}
