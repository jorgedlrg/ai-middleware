package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.InteractionEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.InteractionRepository;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.PerformanceEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.PerformanceId;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.PerformanceRepository;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.SessionEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.SessionRepository;
import com.jorgedelarosa.aimiddleware.application.port.out.DeleteSessionOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionsOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.Interaction;
import com.jorgedelarosa.aimiddleware.domain.session.Mood;
import com.jorgedelarosa.aimiddleware.domain.session.Performance;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.time.Instant;
import java.util.ArrayList;
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
    implements GetSessionByIdOutPort, SaveSessionOutPort, GetSessionsOutPort, DeleteSessionOutPort {

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
        session.getAllInteractions().stream()
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
      interactions.add(InteractionMapper.INSTANCE.toDom(entity, parent));
    }

    List<Performance> performances =
        performanceRepository.findAllByPerformanceIdSession(se.getId()).stream()
            .map((e) -> PerformanceMapper.INSTANCE.toValueObject(e))
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

  @Mapper
  public interface SessionMapper {
    SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

    SessionEntity toEntity(Session session);

    default UUID map(Interaction value) {
      if (value != null) return value.getId();
      else return null;
    }

    default UUID map(Scenario value) {
      return value.getId();
    }
  }

  @Mapper
  public interface InteractionMapper {
    InteractionMapper INSTANCE = Mappers.getMapper(InteractionMapper.class);

    default UUID map(Optional<Interaction> value) {
      if (value.isPresent()) {
        return value.get().getId();
      } else return null;
    }

    @Mapping(source = "interaction.id", target = "id")
    @Mapping(source = "interaction.spokenText", target = "text")
    @Mapping(source = "interaction.thoughtText", target = "thoughts")
    InteractionEntity toEntity(Interaction interaction, UUID session);

    default Interaction toDom(InteractionEntity entity, Interaction parent) {
      Optional<Mood> mood = Optional.empty();
      if (entity.getMood() != null) {
        mood = Optional.ofNullable(Mood.valueOf(entity.getMood()));
      }
      return Interaction.restore(
          entity.getId(),
          entity.getThoughts(),
          entity.getText(),
          "",
          entity.getTimestamp(),
          entity.getRole(),
          entity.getActor(),
          entity.getContext(),
          Optional.ofNullable(parent),
          mood);
    }

    default long map(Instant value) {
      return value.toEpochMilli();
    }

    default String mapOptional(Optional<String> value) {
      return value.orElse(null);
    }

    default String mapMood(Optional<Mood> value) {
      if (value.isPresent()) {
        return value.get().name();
      } else return null;
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
