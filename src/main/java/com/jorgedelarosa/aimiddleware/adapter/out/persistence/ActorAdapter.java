package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorListByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorsOutPort;
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
public class ActorAdapter
    implements GetActorByIdOutPort, GetActorListByIdOutPort, GetActorsOutPort {

  private final ActorRepository actorRepository;
  private final MindRepository mindRepository;
  private final OutfitRepository outfitRepository;

  @Override
  public Optional<Actor> query(UUID id) {
    return actorRepository.findById(id).map((e) -> restoreActor(e));
  }

  @Override
  public List<Actor> query(List<UUID> ids) {
    return actorRepository.findAllById(ids).stream().map((e) -> restoreActor(e)).toList();
  }

  @Override
  public List<Actor> query() {
    return actorRepository.findAll().stream().map((e) -> restoreActor(e)).toList();
  }

  private Actor restoreActor(ActorEntity entity) {
    return Actor.restore(
        entity.getId(),
        entity.getName(),
        entity.getPhysicalDescription(),
        mindRepository.findById(entity.getId()).map(e -> ActorMapper.INSTANCE.toMind(e)),
        outfitRepository.findAllByActor(entity.getId()).stream()
            .map((e) -> ActorMapper.INSTANCE.toOutfit(e))
            .toList(),
        Optional.ofNullable(entity.getCurrentOutfit()));
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
  }
}
