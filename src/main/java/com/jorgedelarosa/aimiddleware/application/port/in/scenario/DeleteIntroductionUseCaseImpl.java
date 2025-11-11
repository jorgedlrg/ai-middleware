package com.jorgedelarosa.aimiddleware.application.port.in.scenario;

import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveScenarioOutPort;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
@Transactional
public class DeleteIntroductionUseCaseImpl implements DeleteIntroductionUseCase {

  private final GetScenarioByIdOutPort getScenarioByIdOutPort;
  private final SaveScenarioOutPort scenarioOutPort;

  @Override
  public void execute(DeleteIntroductionUseCase.Command cmd) {
    Scenario scenario = getScenarioByIdOutPort.query(cmd.scenario()).orElseThrow();
    scenario.deleteIntroduction(cmd.introduction());

    scenarioOutPort.save(scenario);
  }
}
