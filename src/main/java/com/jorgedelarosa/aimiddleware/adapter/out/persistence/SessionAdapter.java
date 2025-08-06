package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.InteractionEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.InteractionRepository;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.PerformanceEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.PerformanceRepository;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.SessionEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.SessionRepository;
import com.jorgedelarosa.aimiddleware.application.port.mapper.SessionMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.DeleteSessionOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionsByScenarioOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionsOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
import com.jorgedelarosa.aimiddleware.domain.session.Interaction;
import com.jorgedelarosa.aimiddleware.domain.session.Performance;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
public class SessionAdapter
    implements GetSessionByIdOutPort,
        SaveSessionOutPort,
        GetSessionsOutPort,
        DeleteSessionOutPort,
        GetSessionsByScenarioOutPort {

  private final SessionRepository sessionRepository;
  private final InteractionRepository interactionRepository;
  private final PerformanceRepository performanceRepository;

  @Override
  public List<Session> queryByScenario(UUID scenario) {
    return sessionRepository.findAllByScenario(scenario).stream()
        .map(e -> restoreSession(e))
        .toList();
  }

  @Override
  public Optional<Session> query(UUID id) {
    return sessionRepository.findById(id).map(e -> restoreSession(e));
  }

  @Override
  public void save(Session session) {
    // Not the most efficient way, but it works for now for a unique 'save' method
    interactionRepository.deleteAllBySession(session.getId());
    performanceRepository.deleteAllByPerformanceIdSession(session.getId());

    List<InteractionEntity> interactions =
        session.getAllInteractions().stream()
            .map((e) -> SessionMapper.INSTANCE.toEntity(e, session.getId()))
            .toList();
    List<PerformanceEntity> performances =
        session.getPerformances().stream()
            .map(e -> SessionMapper.INSTANCE.toEntity(session, e))
            .toList();
    sessionRepository.save(SessionMapper.INSTANCE.toEntity(session));
    interactionRepository.saveAll(interactions);
    performanceRepository.saveAll(performances);
  }

  @Override
  public List<Session> query() {
    return sessionRepository.findAll().stream().map(e -> restoreSession(e)).toList();
  }

  // TODO improve complexity
  private Session restoreSession(SessionEntity se) {
    List<InteractionEntity> ies = interactionRepository.findAllBySession(se.getId());
    List<Interaction> interactions = new ArrayList<>();
    for (InteractionEntity entity : ies) {
      Interaction parent = null;
      if (entity.getParent() != null) {
        parent =
            interactions.stream()
                .filter(e -> e.getId().equals(entity.getParent()))
                .findFirst()
                .orElseThrow();
      }
      interactions.add(SessionMapper.INSTANCE.toDom(entity, parent));
    }

    List<Performance> performances =
        performanceRepository.findAllByPerformanceIdSession(se.getId()).stream()
            .map((e) -> SessionMapper.INSTANCE.toValueObject(e))
            .toList();

    Interaction lastInteraction = null;
    if (se.getLastInteraction() != null) {
      lastInteraction =
          interactions.stream()
              .filter(e -> e.getId().equals(se.getLastInteraction()))
              .findFirst()
              .orElseThrow();
    }
    return Session.restore(
        se.getId(),
        se.getScenario(),
        se.getCurrentContext(),
        interactions,
        performances,
        se.getLocale(),
        lastInteraction);
  }

  @Override
  public void delete(Session session) {
    interactionRepository.deleteAllBySession(session.getId());
    performanceRepository.deleteAllByPerformanceIdSession(session.getId());
    sessionRepository.deleteById(session.getId());
  }
}
