package com.jorgedelarosa.aimiddleware.application.port.mapper;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.InteractionEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.PerformanceEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.PerformanceId;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.SessionEntity;
import com.jorgedelarosa.aimiddleware.application.port.in.session.CreateSessionUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.EditSessionUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.GetSessionDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.session.GetSessionsUseCase;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.scenario.Role;
import com.jorgedelarosa.aimiddleware.domain.scenario.Scenario;
import com.jorgedelarosa.aimiddleware.domain.session.Interaction;
import com.jorgedelarosa.aimiddleware.domain.session.InteractionText;
import com.jorgedelarosa.aimiddleware.domain.session.Mood;
import com.jorgedelarosa.aimiddleware.domain.session.Performance;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

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

  default InteractionEntity toEntity(Interaction interaction, UUID session) {
    InteractionEntity entity = new InteractionEntity();
    entity.setAction(interaction.getActionText().getText());
    entity.setActionReasoning(interaction.getActionText().getReasoning().orElse(null));
    entity.setActor(interaction.getActor());
    entity.setContext(interaction.getContext());
    entity.setId(interaction.getId());
    entity.setMood(interaction.getMood().map(e -> e.name()).orElse(null));
    entity.setParent(interaction.getParent().map(e -> e.getId()).orElse(null));
    entity.setRole(interaction.getRole());
    entity.setSession(session);
    entity.setText(interaction.getSpokenText().getText());
    entity.setTextReasoning(interaction.getSpokenText().getReasoning().orElse(null));
    entity.setThoughts(interaction.getThoughtText().getText());
    entity.setThoughtsReasoning(interaction.getThoughtText().getReasoning().orElse(null));
    entity.setTimestamp(interaction.getTimestamp().toEpochMilli());
    return entity;
  }

  default Interaction toDom(InteractionEntity entity, Interaction parent) {
    Optional<Mood> mood = Optional.empty();
    if (entity.getMood() != null) {
      mood = Optional.ofNullable(Mood.valueOf(entity.getMood()));
    }
    return Interaction.restore(
        entity.getId(),
        new InteractionText(
            entity.getThoughts(), Optional.ofNullable(entity.getThoughtsReasoning())),
        new InteractionText(entity.getText(), Optional.ofNullable(entity.getTextReasoning())),
        new InteractionText(entity.getAction(), Optional.ofNullable(entity.getActionReasoning())),
        entity.getTimestamp(),
        entity.getRole(),
        entity.getActor(),
        entity.getContext(),
        Optional.ofNullable(parent),
        mood);
  }

  default GetSessionDetailsUseCase.PerformanceDto toDto(
      Actor actor, Role role, List<Interaction> interactions) {
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

  default GetSessionDetailsUseCase.InteractionDto toDto(
      Interaction dom, Actor actor, List<Interaction> siblings) {
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
        dom.getThoughtText().getText(),
        dom.getActionText().getText(),
        dom.getSpokenText().getText(),
        portrait,
        siblings.indexOf(dom) + 1,
        siblings.size(),
        moodName,
        mooodEmoji,
        dom.getThoughtText().getReasoning().orElse(null),
        dom.getActionText().getReasoning().orElse(null),
        dom.getSpokenText().getReasoning().orElse(null));
  }
}
