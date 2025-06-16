package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorListByIdOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.actor.Mind;
import com.jorgedelarosa.aimiddleware.domain.actor.Outfit;
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
  private final OutfitRepository outfitRepository;

  @Override
  public Optional<Actor> query(UUID id) {
    return actorRepository
        .findById(id)
        .map(
            (e) ->
                ActorMapper.INSTANCE.toDom(
                    e,
                    mindRepository.findById(e.getId()),
                    outfitRepository.findAllByActor(e.getId())));
  }

  @Override
  public List<Actor> query(List<UUID> ids) {
    return actorRepository.findAllById(ids).stream()
        .map(
            (e) ->
                ActorMapper.INSTANCE.toDom(
                    e,
                    mindRepository.findById(e.getId()),
                    outfitRepository.findAllByActor(e.getId())))
        .toList();
  }

  @Mapper
  public interface ActorMapper {
    ActorMapper INSTANCE = Mappers.getMapper(ActorMapper.class);

    default Mind toMind(MindEntity entity) {
      return Mind.restore(entity.getActor(), entity.getPersonality());
    }

    default Outfit toOutfit(OutfitEntity entity) {
      return Outfit.restore(entity.getId(), entity.getDescription());
    }

    default Actor toDom(
        ActorEntity entity, Optional<MindEntity> mindEntity, List<OutfitEntity> outfitEntities) {
      Optional<Mind> mind =
          mindEntity.isPresent() ? Optional.of(toMind(mindEntity.get())) : Optional.empty();
      List<Outfit> outfits = outfitEntities.stream().map((e) -> toOutfit(e)).toList();
      return Actor.restore(
          entity.getId(),
          entity.getName(),
          entity.getPhysicalDescription(),
          mind,
          outfits,
          Optional.ofNullable(entity.getCurrentOutfit()));
    }
  }
}
