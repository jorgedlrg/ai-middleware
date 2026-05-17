package com.jorgedelarosa.aimiddleware.application.port.in.session;

import com.jorgedelarosa.aimiddleware.application.port.mapper.SessionMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
public class GetSessionDetailsUseCaseImpl implements GetSessionDetailsUseCase {

  private final GetSessionByIdOutPort getSessionByIdOutPort;
  private final GetActorByIdOutPort getActorByIdOutPort;
  private final GetScenarioByIdOutPort getScenarioByIdOutPort;

  @Override
  public SessionDto execute(Command cmd) {
    Session session = getSessionByIdOutPort.query(cmd.session()).orElseThrow();
    Scenario scenario = getScenarioByIdOutPort.query(session.getScenario()).orElseThrow();

    SessionDto dto =
        new SessionDto(
            session.getId(),
            session.getScenario(),
            session.getCurrentContext(),
            session.getLocale(),
            session.getPerformances().stream()
                .map(
                    e ->
                        SessionMapper.INSTANCE.toDto(
                            getActorByIdOutPort.query(e.getActor()).orElseThrow(),
                            findRole(scenario, e.getRole()),
                            session.getCurrentInteractions()))
                .toList(),
            session.getCurrentInteractions().stream()
                .map(
                    (e) ->
                        SessionMapper.INSTANCE.toDto(
                            e,
                            getActorByIdOutPort.query(e.getActor()).orElseThrow(),
                            session.getChildren(e.getParent().orElse(null))))
                .toList());

    return dto;
  }

  private Role findRole(Scenario scenario, UUID role) {
    return scenario.getRoles().stream()
        .filter(e -> e.getId().equals(role))
        .findFirst()
        .orElseThrow();
  }
}
