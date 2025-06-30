package com.jorgedelarosa.aimiddleware.application.port.in.scenario;

import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveScenarioOutPort;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
@Transactional
public class DeleteRoleUseCaseImpl implements DeleteRoleUseCase {
  private final GetScenarioByIdOutPort getScenarioByIdOutPort;
  private final SaveScenarioOutPort scenarioOutPort;

  @Override
  public void execute(Command cmd) {
    Scenario scenario = getScenarioByIdOutPort.query(cmd.scenarioId()).orElseThrow();
    scenario.deleteRole(cmd.roleId());

    scenarioOutPort.save(scenario);
  }
}
