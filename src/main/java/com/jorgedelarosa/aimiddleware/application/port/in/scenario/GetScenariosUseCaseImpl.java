package com.jorgedelarosa.aimiddleware.application.port.in.scenario;

import com.jorgedelarosa.aimiddleware.application.port.out.GetScenariosOutPort;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import java.util.List;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
public class GetScenariosUseCaseImpl implements GetScenariosUseCase {
  private final GetScenariosOutPort getScenariosOutPort;

  @Override
  public List<ScenarioDto> execute(Command cmd) {
    return getScenariosOutPort.query().stream().map(e -> ScenarioMapper.INSTANCE.toDto(e)).toList();
  }

  @Mapper
  public interface ScenarioMapper {
    ScenarioMapper INSTANCE = Mappers.getMapper(ScenarioMapper.class);

    default GetScenariosUseCase.ScenarioDto toDto(Scenario dom) {
      return new GetScenariosUseCase.ScenarioDto(
          dom.getId(), dom.getName(), dom.getContexts().size(), dom.getRoles().size());
    }
  }
}
