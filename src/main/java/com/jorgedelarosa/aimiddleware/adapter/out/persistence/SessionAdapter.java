package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
public class SessionAdapter implements GetSessionByIdOutPort, SaveSessionOutPort {

  private final SessionRepository sessionRepository;
  private final ScenarioRepository scenarioRepository;

  @Override
  public Optional<Session> query(UUID id) {
    Optional<SessionEntity> sessionEntity = sessionRepository.findById(id);
    if (sessionEntity.isPresent()) {
      SessionEntity se = sessionEntity.get();
      ScenarioEntity scenarioEnt = scenarioRepository.findById(se.getScenario()).orElseThrow();
      Scenario scenario = Scenario.restore(scenarioEnt.getId());
      // TODO: restore interactions as well
      return Optional.of(Session.restore(se.getId(), scenario, new ArrayList<>()));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public void save(Session session) {
    sessionRepository.save(SessionEntityMapper.INSTANCE.toEntity(session));
  }

  @Mapper
  public interface SessionEntityMapper {
    SessionEntityMapper INSTANCE = Mappers.getMapper(SessionEntityMapper.class);

    SessionEntity toEntity(Session session);

    default UUID map(Scenario value) {
      return value.getId();
    }
  }
}
