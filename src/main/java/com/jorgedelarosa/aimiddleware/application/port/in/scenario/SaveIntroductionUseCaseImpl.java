package com.jorgedelarosa.aimiddleware.application.port.in.scenario;

import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveScenarioOutPort;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
@Transactional
public class SaveIntroductionUseCaseImpl implements SaveIntroductionUseCase {
  private final GetScenarioByIdOutPort getScenarioByIdOutPort;
  private final SaveScenarioOutPort scenarioOutPort;

  @Override
  public UUID execute(SaveIntroductionUseCase.Command cmd) {
    Scenario scenario = getScenarioByIdOutPort.query(cmd.scenario()).orElseThrow();
    if (cmd.introduction() == null) {
      scenario.addNewIntroduction(
          cmd.spokenText(),
          cmd.thoughtText(),
          cmd.actionText(),
          scenario.getRoles().stream()
              .filter(e -> e.getId().equals(cmd.performer()))
              .findFirst()
              .orElseThrow(),
          scenario.getContexts().stream()
              .filter(e -> e.getId().equals(cmd.context()))
              .findFirst()
              .orElseThrow());
    } else {
      scenario.modifyIntroduction(
          cmd.introduction(), cmd.spokenText(), cmd.thoughtText(), cmd.actionText());
    }
    scenarioOutPort.save(scenario);

    if (cmd.introduction() != null) {
      return cmd.introduction();
    } else {
      return scenario.getIntroductions().getLast().getId();
    }
  }
}
