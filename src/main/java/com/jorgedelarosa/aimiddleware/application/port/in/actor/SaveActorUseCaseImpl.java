package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetOutfitByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveActorOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
@Transactional
public class SaveActorUseCaseImpl implements SaveActorUseCase {

  private final GetActorByIdOutPort getActorByIdOutPort;
  private final GetOutfitByIdOutPort getOutfitByIdOutPort;
  private final SaveActorOutPort saveActorOutPort;

  @Override
  public UUID execute(Command cmd) {
    Actor actor;
    if (cmd.id() == null) {
      // CREATE
      actor = Actor.create(cmd.name(), cmd.profile(), cmd.physicalDescription(), cmd.personality());
    } else {
      // UPDATE
      actor = getActorByIdOutPort.query(cmd.id()).orElseThrow();
      actor.setName(cmd.name());
      actor.setProfile(cmd.profile());
      actor.setPhysicalDescription(cmd.physicalDescription());
      actor.setPersonality(cmd.personality());
    }
    actor.setPortrait(cmd.portrait());
    if (cmd.outfit() != null) {
      getOutfitByIdOutPort.query(cmd.outfit()).orElseThrow();
    }
    actor.chooseOutfit(cmd.outfit());

    saveActorOutPort.save(actor);
    return actor.getId();
  }
}
