package com.jorgedelarosa.aimiddleware.application.port.mapper;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.ActorEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.MindEntity;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorsUseCase;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.actor.Mind;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author jorge
 */
@Mapper
public interface ActorMapper {
  ActorMapper INSTANCE = Mappers.getMapper(ActorMapper.class);

  default Optional<GetActorDetailsUseCase.MindDto> map(Optional<Mind> value) {
    return value.map(e -> new GetActorDetailsUseCase.MindDto(e.getPersonality()));
  }

  GetActorDetailsUseCase.ActorDto toDetailDto(Actor dom);

  GetActorsUseCase.ActorDto toDto(Actor dom);

  default ActorEntity toEntity(Actor dom) {
    ActorEntity ae = new ActorEntity();
    ae.setId(dom.getId());
    ae.setName(dom.getName());
    ae.setProfile(dom.getProfile());
    ae.setPhysicalDescription(dom.getPhysicalDescription());
    dom.getCurrentOutfit().ifPresent(e -> ae.setCurrentOutfit(e));
    return ae;
  }

  default Mind toMind(MindEntity entity) {
    return Mind.restore(entity.getActor(), entity.getPersonality());
  }

  @Mapping(target = "actor", source = "id")
  MindEntity toEntity(Mind dom);
}
