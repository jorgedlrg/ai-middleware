package com.jorgedelarosa.aimiddleware.application.port.in;

import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
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
    return ScenarioMapper.INSTANCE.toDto(
        getScenarioByIdOutPort.query(cmd.scenarioId()).orElseThrow());
  }

  @Mapper
  public interface ScenarioMapper {
    ScenarioMapper INSTANCE = Mappers.getMapper(ScenarioMapper.class);

    GetScenarioDetailsUseCase.ScenarioDto toDto(Scenario dom);
  }
}
