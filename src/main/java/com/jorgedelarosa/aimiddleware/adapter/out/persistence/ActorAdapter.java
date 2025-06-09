package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.domain.Actor;
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
public class ActorAdapter implements GetActorByIdOutPort {

  private final ActorRepository actorRepository;

  @Override
  public Optional<Actor> query(UUID id) {
    return actorRepository.findById(id).map(ActorMapper.INSTANCE::toDom);
  }

  @Mapper
  public interface ActorMapper {
    ActorMapper INSTANCE = Mappers.getMapper(ActorMapper.class);

    default Actor toDom(ActorEntity entity) {
      return Actor.restore(entity.getId(), entity.getName(), entity.getPhysicalDescription());
    }
  }
}
