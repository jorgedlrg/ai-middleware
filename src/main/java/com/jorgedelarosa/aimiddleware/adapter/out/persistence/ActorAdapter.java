package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.ActorEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.ActorRepository;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.MindEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.MindRepository;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.OutfitEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.OutfitRepository;
import com.jorgedelarosa.aimiddleware.application.port.out.DeleteActorOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorListByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorsOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveActorOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.actor.Mind;
import com.jorgedelarosa.aimiddleware.domain.actor.Outfit;
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
public class ActorAdapter
    implements GetActorByIdOutPort,
        GetActorListByIdOutPort,
        GetActorsOutPort,
        SaveActorOutPort,
        DeleteActorOutPort {

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

  @Override
  public void save(Actor actor) {
    mindRepository.deleteById(actor.getId());
    actorRepository.save(ActorMapper.INSTANCE.toEntity(actor));
    actor.getMind().ifPresent(e -> mindRepository.save(ActorMapper.INSTANCE.toEntity(e)));
  }

  @Override
  public void delete(Actor actor) {
    mindRepository.deleteById(actor.getId());
    actorRepository.deleteById(actor.getId());
  }

  @Mapper
  public interface ActorMapper {
    ActorMapper INSTANCE = Mappers.getMapper(ActorMapper.class);

    default ActorEntity toEntity(Actor dom) {
      ActorEntity ae = new ActorEntity();
      ae.setId(dom.getId());
      ae.setName(dom.getName());
      ae.setPhysicalDescription(dom.getPhysicalDescription());
      dom.getCurrentOutfit().ifPresent(e -> ae.setCurrentOutfit(e.getId()));
      return ae;
    }

    default Mind toMind(MindEntity entity) {
      return Mind.restore(entity.getActor(), entity.getPersonality());
    }

    @Mapping(target = "actor", source = "id")
    MindEntity toEntity(Mind dom);

    default Outfit toOutfit(OutfitEntity entity) {
      return Outfit.restore(entity.getId(), entity.getDescription());
    }
  }
}
