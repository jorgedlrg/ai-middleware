package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorListByIdOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.actor.Mind;
import java.util.List;
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
public class ActorAdapter implements GetActorByIdOutPort, GetActorListByIdOutPort {

  private final ActorRepository actorRepository;
  private final MindRepository mindRepository;

  @Override
  public Optional<Actor> query(UUID id) {
    return actorRepository
        .findById(id)
        .map((e) -> ActorMapper.INSTANCE.toDom(e, mindRepository.findById(e.getId())));
  }

  @Override
  public List<Actor> query(List<UUID> ids) {
    return actorRepository.findAllById(ids).stream()
        .map((e) -> ActorMapper.INSTANCE.toDom(e, mindRepository.findById(e.getId())))
        .toList();
  }

  @Mapper
  public interface ActorMapper {
    ActorMapper INSTANCE = Mappers.getMapper(ActorMapper.class);

    default Mind toMind(MindEntity entity){
      return Mind.restore(entity.getActor(), entity.getPersonality());
    }

    default Actor toDom(ActorEntity entity, Optional<MindEntity> mindEntity) {
      Optional<Mind> mind =
          mindEntity.isPresent() ? Optional.of(toMind(mindEntity.get())) : Optional.empty();
      return Actor.restore(entity.getId(), entity.getName(), entity.getPhysicalDescription(), mind);
    }
  }
}
