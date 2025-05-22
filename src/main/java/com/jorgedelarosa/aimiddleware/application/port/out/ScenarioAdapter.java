package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
public class ScenarioAdapter implements GetScenarioByIdOutPort{

  @Override
  public Scenario query() {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }
}
