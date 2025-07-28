package com.jorgedelarosa.aimiddleware.application.port.in.scenario;

import com.jorgedelarosa.aimiddleware.application.port.mapper.ScenarioMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.GetScenariosOutPort;
import java.util.List;
import lombok.AllArgsConstructor;
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
}
