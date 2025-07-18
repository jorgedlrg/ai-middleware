package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.filesystem.AssetRepository;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.ActorEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.ActorRepository;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.MindEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.MindRepository;
import com.jorgedelarosa.aimiddleware.application.port.out.DeleteActorOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorListByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorsOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveActorOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.actor.Mind;
import com.jorgedelarosa.aimiddleware.domain.session.Mood;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  private final AssetRepository assetRepository;

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
    byte[] portrait = assetRepository.load("actors/" + entity.getId() + "/portrait.png");
    Map<Mood, List<byte[]>> moodPortraits = new HashMap<>();
    for (Mood mood : Mood.values()) {
      moodPortraits.put(
          mood,
          assetRepository.loadAssets("actors/" + entity.getId() + "/" + mood.name().toLowerCase()));
    }

    return Actor.restore(
        entity.getId(),
        entity.getName(),
        entity.getPhysicalDescription(),
        mindRepository.findById(entity.getId()).map(e -> ActorMapper.INSTANCE.toMind(e)),
        Optional.ofNullable(entity.getCurrentOutfit()),
        portrait,
        moodPortraits);
  }

  @Override
  public void save(Actor actor) {
    mindRepository.deleteById(actor.getId());
    assetRepository.delete("actors/" + actor.getId() + "/portrait.png");

    actorRepository.save(ActorMapper.INSTANCE.toEntity(actor));
    actor.getMind().ifPresent(e -> mindRepository.save(ActorMapper.INSTANCE.toEntity(e)));

    if (actor.getPortrait().length > 0) {
      assetRepository.save("actors/" + actor.getId(), "/portrait.png", actor.getPortrait());
    }
  }

  @Override
  public void delete(Actor actor) {
    mindRepository.deleteById(actor.getId());
    actorRepository.deleteById(actor.getId());
    assetRepository.delete("actors/" + actor.getId());
  }

  @Mapper
  public interface ActorMapper {
    ActorMapper INSTANCE = Mappers.getMapper(ActorMapper.class);

    default ActorEntity toEntity(Actor dom) {
      ActorEntity ae = new ActorEntity();
      ae.setId(dom.getId());
      ae.setName(dom.getName());
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
}
