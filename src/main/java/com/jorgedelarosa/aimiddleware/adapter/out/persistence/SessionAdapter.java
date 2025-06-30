package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.InteractionEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.InteractionRepository;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.PerformanceEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.PerformanceId;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.PerformanceRepository;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.SessionEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.SessionRepository;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionsOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.Interaction;
import com.jorgedelarosa.aimiddleware.domain.session.Performance;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
public class SessionAdapter
    implements GetSessionByIdOutPort, SaveSessionOutPort, GetSessionsOutPort {

  private final SessionRepository sessionRepository;
  private final InteractionRepository interactionRepository;
  private final PerformanceRepository performanceRepository;

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
        session.getInteractions().stream()
            .map((e) -> InteractionMapper.INSTANCE.toEntity(e, session.getId()))
            .toList();
    List<PerformanceEntity> performances =
        session.getPerformances().stream()
            .map(e -> PerformanceMapper.INSTANCE.toEntity(session, e))
            .toList();
    sessionRepository.save(SessionMapper.INSTANCE.toEntity(session));
    interactionRepository.saveAll(interactions);
    performanceRepository.saveAll(performances);
  }

  @Override
  public List<Session> query() {
    return sessionRepository.findAll().stream().map(e -> restoreSession(e)).toList();
  }

  private Session restoreSession(SessionEntity se) {
    List<Interaction> interactions =
        interactionRepository.findAllBySession(se.getId()).stream()
            .map((e) -> InteractionMapper.INSTANCE.toDom(e))
            .toList();
    List<Performance> performances =
        performanceRepository.findAllByPerformanceIdSession(se.getId()).stream()
            .map((e) -> PerformanceMapper.INSTANCE.toValueObject(e))
            .toList();

    return Session.restore(
        se.getId(),
        se.getScenario(),
        se.getCurrentContext(),
        interactions,
        performances,
        se.getLocale());
  }

  @Mapper
  public interface SessionMapper {
    SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

    SessionEntity toEntity(Session session);

    default UUID map(Scenario value) {
      return value.getId();
    }
  }

  @Mapper
  public interface InteractionMapper {
    InteractionMapper INSTANCE = Mappers.getMapper(InteractionMapper.class);

    @Mapping(source = "interaction.id", target = "id")
    @Mapping(source = "interaction.spokenText", target = "text")
    InteractionEntity toEntity(Interaction interaction, UUID session);

    default Interaction toDom(InteractionEntity entity) {
      return Interaction.restore(
          entity.getId(),
          "",
          entity.getText(),
          "",
          entity.getTimestamp(),
          entity.getRole(),
          entity.getActor(),
          entity.getContext());
    }

    default long map(Instant value) {
      return value.toEpochMilli();
    }
  }

  @Mapper
  public interface PerformanceMapper {
    PerformanceMapper INSTANCE = Mappers.getMapper(PerformanceMapper.class);

    default Performance toValueObject(PerformanceEntity a) {
      return new Performance(a.getActor(), a.getPerformanceId().getRole());
    }

    default PerformanceEntity toEntity(Session se, Performance p) {
      return new PerformanceEntity(new PerformanceId(se.getId(), p.getRole()), p.getActor());
    }
  }
}
