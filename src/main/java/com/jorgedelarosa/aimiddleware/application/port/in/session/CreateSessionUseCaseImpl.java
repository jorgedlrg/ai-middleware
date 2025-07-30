package com.jorgedelarosa.aimiddleware.application.port.in.session;

import com.jorgedelarosa.aimiddleware.application.port.out.GetScenarioByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
import com.jorgedelarosa.aimiddleware.domain.scenario.Introduction;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.Performance;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
@Transactional
public class CreateSessionUseCaseImpl implements CreateSessionUseCase {
  private final SaveSessionOutPort saveSessionOutPort;
  private final GetScenarioByIdOutPort getScenarioByIdOutPort;

  @Override
  public UUID execute(Command cmd) {
    Session session =
        Session.create(
            cmd.scenario(),
            cmd.currentContext(),
            cmd.performances().stream().map(e -> SessionMapper.INSTANCE.toDom(e)).toList(),
            cmd.locale());

    if (cmd.introduction().isPresent()) {
      Introduction intro =
          getScenarioByIdOutPort.query(cmd.scenario()).orElseThrow().getIntroductions().stream()
              .filter(i -> i.getId().equals(cmd.introduction().get()))
              .findFirst()
              .orElseThrow();
      session.interact(
          intro.getThoughtText().orElse(null),
          intro.getActionText().orElse(null),
          intro.getSpokenText(),
          intro.getPerformer().getId(),
          Optional.empty());
    }
    saveSessionOutPort.save(session);

    return session.getId();
  }

  @Mapper
  public interface SessionMapper {
    SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

    Performance toDom(CreateSessionUseCase.PerformanceDto dto);
  }
}
