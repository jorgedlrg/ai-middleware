package com.jorgedelarosa.aimiddleware.application.port.mapper;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.InteractionEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.PerformanceEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.PerformanceId;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.SessionEntity;
import com.jorgedelarosa.aimiddleware.application.port.in.session.CreateSessionUseCase;
import com.jorgedelarosa.aimiddleware.domain.session.Performance;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.jorgedelarosa.aimiddleware.application.port.in.session.EditSessionUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.GetSessionDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.GetSessionsUseCase;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.Interaction;
import com.jorgedelarosa.aimiddleware.domain.session.Mood;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.mapstruct.Mapping;

/**
 * @author jorge
 */
@Mapper
public interface SessionMapper {
  SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

  Performance toDom(CreateSessionUseCase.PerformanceDto dto);

  Performance toDom(EditSessionUseCase.PerformanceDto dto);

  @Mapping(target = "session", source = "dom.id")
  @Mapping(target = "scenario", source = "sc.name")
  GetSessionsUseCase.SessionDto toDto(Session dom, Scenario sc);

  SessionEntity toEntity(Session session);

  default UUID map(Interaction value) {
    if (value != null) return value.getId();
    else return null;
  }

  default UUID map(Scenario value) {
    return value.getId();
  }

  default Performance toValueObject(PerformanceEntity a) {
    return new Performance(a.getActor(), a.getPerformanceId().getRole());
  }

  default PerformanceEntity toEntity(Session se, Performance p) {
    return new PerformanceEntity(new PerformanceId(se.getId(), p.getRole()), p.getActor());
  }

  default UUID map(Optional<Interaction> value) {
    if (value.isPresent()) {
      return value.get().getId();
    } else return null;
  }

  @Mapping(source = "interaction.id", target = "id")
  @Mapping(source = "interaction.spokenText", target = "text")
  @Mapping(source = "interaction.thoughtText", target = "thoughts")
  @Mapping(source = "interaction.actionText", target = "action")
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
        entity.getAction(),
        entity.getTimestamp(),
        entity.getRole(),
        entity.getActor(),
        entity.getContext(),
        Optional.ofNullable(parent),
        mood);
  }

  default long mapMillis(Instant value) {
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
  
  default GetSessionDetailsUseCase.PerformanceDto toDto(Actor actor, Role role, List<Interaction> interactions) {
      byte[] portrait;
      List<Interaction> actorInteractions =
          interactions.stream().filter(e -> e.getActor().equals(actor.getId())).toList();
      if (!actorInteractions.isEmpty()) {
        if (actorInteractions.getLast().getMood().isPresent()) {
          portrait = actor.getMoodPortrait(actorInteractions.getLast().getMood().get());
        } else {
          portrait = actor.getPortrait();
        }
      } else {
        portrait = actor.getPortrait();
      }

      return new GetSessionDetailsUseCase.PerformanceDto(
          actor.getId(), role.getId(), actor.getName(), role.getName(), portrait);
    }

    default GetSessionDetailsUseCase.InteractionDto toDto(Interaction dom, Actor actor, List<Interaction> siblings) {
      String moodName = "";
      String mooodEmoji = "";
      if (dom.getMood().isPresent()) {
        moodName = dom.getMood().get().name().toLowerCase();
        mooodEmoji = dom.getMood().get().getEmoji();
      }
      byte[] portrait;
      if (dom.getMood().isPresent()) {
        portrait = actor.getMoodPortrait(dom.getMood().get());
      } else {
        portrait = actor.getPortrait();
      }
      return new GetSessionDetailsUseCase.InteractionDto(
          dom.getId(),
          dom.getTimestamp(),
          actor.getName(),
          dom.getThoughtText(),
          dom.getActionText(),
          dom.getSpokenText(),
          portrait,
          siblings.indexOf(dom) + 1,
          siblings.size(),
          moodName,
          mooodEmoji);
    }
}
