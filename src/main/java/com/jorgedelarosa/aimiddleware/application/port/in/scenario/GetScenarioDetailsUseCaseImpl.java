package com.jorgedelarosa.aimiddleware.application.port.in.scenario;

import com.jorgedelarosa.aimiddleware.application.port.mapper.ScenarioMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
public class GetScenarioDetailsUseCaseImpl implements GetScenarioDetailsUseCase {

  private final GetScenarioByIdOutPort getScenarioByIdOutPort;

  @Override
  public ScenarioDto execute(Command cmd) {
    return ScenarioMapper.INSTANCE.toDetailsDto(
        getScenarioByIdOutPort.query(cmd.scenarioId()).orElseThrow());
  }
}
