package com.jorgedelarosa.aimiddleware.application.port.in.scenario;

import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveScenarioOutPort;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
@Transactional
public class SaveScenarioUseCaseImpl implements SaveScenarioUseCase {

  private final GetScenarioByIdOutPort getScenarioByIdOutPort;
  private final SaveScenarioOutPort scenarioOutPort;

  @Override
  public UUID execute(SaveScenarioUseCase.Command cmd) {
    Scenario scenario;
    if (cmd.id() == null) {
      // CREATE
      scenario = Scenario.create(cmd.name(), cmd.description());
    } else {
      // UPDATE
      scenario = getScenarioByIdOutPort.query(cmd.id()).orElseThrow();

      scenario.setName(cmd.name());
      scenario.setDescription(cmd.description());
    }

    scenarioOutPort.save(scenario);
    return scenario.getId();
  }
}
