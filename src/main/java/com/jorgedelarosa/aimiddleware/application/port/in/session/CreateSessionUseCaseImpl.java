package com.jorgedelarosa.aimiddleware.application.port.in.session;

import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
import com.jorgedelarosa.aimiddleware.domain.session.Performance;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
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

  @Override
  public UUID execute(Command cmd) {
    Session session =
        Session.create(
            cmd.scenario(),
            cmd.currentContext(),
            cmd.performances().stream().map(e -> SessionMapper.INSTANCE.toDom(e)).toList(),
            cmd.locale());
    saveSessionOutPort.save(session);

    return session.getId();
  }

  @Mapper
  public interface SessionMapper {
    SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

    Performance toDom(CreateSessionUseCase.PerformanceDto dto);
  }
}
