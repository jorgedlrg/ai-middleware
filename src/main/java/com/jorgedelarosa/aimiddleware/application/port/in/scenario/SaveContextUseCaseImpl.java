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
public class SaveContextUseCaseImpl implements SaveContextUseCase {

  private final GetScenarioByIdOutPort getScenarioByIdOutPort;
  private final SaveScenarioOutPort scenarioOutPort;

  @Override
  public UUID execute(Command cmd) {
    Scenario scenario = getScenarioByIdOutPort.query(cmd.scenario()).orElseThrow();
    if (cmd.id() == null) {
      scenario.addNewContext(cmd.name(), cmd.physicalDescription());
    } else {
      scenario.modifyContext(cmd.id(), cmd.name(), cmd.physicalDescription());
    }
    scenarioOutPort.save(scenario);

    if (cmd.id() != null) {
      return cmd.id();
    } else {
      return scenario.getContexts().getLast().getId();
    }
  }
}
