package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.Interaction;
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
public class SessionAdapter implements GetSessionByIdOutPort, SaveSessionOutPort {

  private final SessionRepository sessionRepository;
  private final InteractionRepository interactionRepository;

  @Override
  public Optional<Session> query(UUID id) {
    Optional<SessionEntity> sessionEntity = sessionRepository.findById(id);
    if (sessionEntity.isPresent()) {
      SessionEntity se = sessionEntity.get();
      List<Interaction> interactions =
          interactionRepository.findAllBySession(se.getId()).stream()
              .map((e) -> InteractionEntityMapper.INSTANCE.toDom(e))
              .toList();
      return Optional.of(Session.restore(se.getId(), se.getScenario(), interactions));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public void save(Session session) {
    sessionRepository.save(SessionEntityMapper.INSTANCE.toEntity(session));
    List<InteractionEntity> interactions =
        session.getInteractions().stream()
            .map((e) -> InteractionEntityMapper.INSTANCE.toEntity(e, session.getId()))
            .toList();
    interactionRepository.saveAll(interactions);
  }

  @Mapper
  public interface SessionEntityMapper {
    SessionEntityMapper INSTANCE = Mappers.getMapper(SessionEntityMapper.class);

    SessionEntity toEntity(Session session);

    default UUID map(Scenario value) {
      return value.getId();
    }
  }

  @Mapper
  public interface InteractionEntityMapper {
    InteractionEntityMapper INSTANCE = Mappers.getMapper(InteractionEntityMapper.class);

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
          entity.isUser(),
          entity.getContext());
    }

    default long map(Instant value) {
      return value.toEpochMilli();
    }
  }
}
