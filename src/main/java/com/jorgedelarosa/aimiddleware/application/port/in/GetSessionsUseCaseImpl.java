package com.jorgedelarosa.aimiddleware.application.port.in;

import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionsOutPort;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.List;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
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

  @Mapper
  public interface SessionMapper {
    SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

    @Mapping(target = "session", source = "dom.id")
    @Mapping(target = "scenario", source = "sc.name")
    GetSessionsUseCase.SessionDto toDto(Session dom, Scenario sc);
  }
}
