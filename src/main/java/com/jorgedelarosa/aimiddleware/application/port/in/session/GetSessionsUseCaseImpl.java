package com.jorgedelarosa.aimiddleware.application.port.in.session;

import com.jorgedelarosa.aimiddleware.application.port.mapper.SessionMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionsOutPort;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
public class GetSessionsUseCaseImpl implements GetSessionsUseCase {

  private final GetSessionsOutPort getSessionsOutPort;
  private final GetScenarioByIdOutPort getScenarioByIdOutPort;

  @Override
  public List<SessionDto> execute(Command cmd) {
    return getSessionsOutPort.query().stream()
        .map(
            e ->
                SessionMapper.INSTANCE.toDto(
                    e, getScenarioByIdOutPort.query(e.getScenario()).orElseThrow()))
        .toList();
  }
}
