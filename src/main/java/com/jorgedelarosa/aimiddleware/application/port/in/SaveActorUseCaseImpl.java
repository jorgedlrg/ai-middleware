package com.jorgedelarosa.aimiddleware.application.port.in;

import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveActorOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
@Transactional
public class SaveActorUseCaseImpl implements SaveActorUseCase {

  private final GetActorByIdOutPort getActorByIdOutPort;
  private final SaveActorOutPort saveActorOutPort;

  @Override
  public UUID execute(Command cmd) {
    Actor actor;
    if (cmd.id() == null) {
      // CREATE
      actor = Actor.create(cmd.name(), cmd.physicalDescription(), Optional.empty());
    } else {
      // UPDATE
      actor = getActorByIdOutPort.query(cmd.id()).orElseThrow();

      actor.setName(cmd.name());
      actor.setPhysicalDescription(cmd.physicalDescription());
    }

    saveActorOutPort.save(actor);
    return actor.getId();
  }
}
