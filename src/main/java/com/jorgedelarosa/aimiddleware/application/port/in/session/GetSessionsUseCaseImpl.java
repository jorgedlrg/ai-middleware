package com.jorgedelarosa.aimiddleware.application.port.in.session;

import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionsOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.Comparator;
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
  private final GetActorByIdOutPort getActorByIdOutPort;

  @Override
  public List<SessionDto> execute(Command cmd) {
    return getSessionsOutPort.query().stream()
        .map(this::toDto)
        .sorted(
            Comparator.comparing(
                (SessionDto dto) ->
                    dto.lastActivity() != null ? dto.lastActivity() : java.time.Instant.MIN,
                Comparator.nullsLast(Comparator.reverseOrder())))
        .toList();
  }

  private SessionDto toDto(Session session) {
    var scenario = getScenarioByIdOutPort.query(session.getScenario()).orElseThrow();
    var scenarioRoles = scenario.getRoles();

    List<String> participantNames =
        session.getPerformances().stream()
            .map(
                p -> {
                  String actorName =
                      getActorByIdOutPort.query(p.getActor()).map(Actor::getName).orElse("Unknown");
                  String roleName =
                      scenarioRoles.stream()
                          .filter(r -> r.getId().equals(p.getRole()))
                          .findFirst()
                          .map(r -> r.getName())
                          .orElse("Unknown");
                  return actorName + " (" + roleName + ")";
                })
            .toList();

    var lastActivity =
        session.getLastInteraction() != null ? session.getLastInteraction().getTimestamp() : null;

    return new SessionDto(
        session.getId(),
        scenario.getName(),
        lastActivity,
        session.getAllInteractions().size(),
        participantNames);
  }
}
