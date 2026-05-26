package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.filesystem.AssetRepository;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.ActorEntity;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.ActorRepository;
import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.MindRepository;
import com.jorgedelarosa.aimiddleware.application.port.mapper.ActorMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.DeleteActorOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorListByCurrentOutfitOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorListByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorsOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveActorOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
public class ActorAdapter
    implements GetActorByIdOutPort,
        GetActorListByIdOutPort,
        GetActorsOutPort,
        SaveActorOutPort,
        DeleteActorOutPort,
        GetActorListByCurrentOutfitOutPort {

  private final ActorRepository actorRepository;
  private final MindRepository mindRepository;
  private final AssetRepository assetRepository;

  @Override
  public List<Actor> queryActors(UUID outfit) {
    return actorRepository.findAllByCurrentOutfit(outfit).stream()
        .map(e -> restoreActor(e))
        .toList();
  }

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
        entity.getProfile(),
        entity.getPhysicalDescription(),
        mindRepository.findById(entity.getId()).map(e -> ActorMapper.INSTANCE.toMind(e)),
        Optional.ofNullable(entity.getCurrentOutfit()),
        Instant.ofEpochMilli(entity.getCreatedAt()),
        Instant.ofEpochMilli(entity.getUpdatedAt()));
  }

  @Override
  public void save(Actor actor, byte[] portrait) {
    mindRepository.deleteById(actor.getId());
    assetRepository.delete("actors/" + actor.getId() + "/portrait.png");

    actorRepository.save(ActorMapper.INSTANCE.toEntity(actor));
    actor.getMind().ifPresent(e -> mindRepository.save(ActorMapper.INSTANCE.toEntity(e)));

    if (portrait != null && portrait.length > 0) {
      assetRepository.save("actors/" + actor.getId(), "/portrait.png", portrait);
    }
  }

  @Override
  public void delete(Actor actor) {
    mindRepository.deleteById(actor.getId());
    actorRepository.deleteById(actor.getId());
    assetRepository.delete("actors/" + actor.getId());
  }
}
