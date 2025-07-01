package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import com.jorgedelarosa.aimiddleware.application.port.out.DeleteActorOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
@Transactional
public class DeleteActorUseCaseImpl implements DeleteActorUseCase {

  private final GetActorByIdOutPort getActorByIdOutPort;
  private final DeleteActorOutPort deleteActorOutPort;

  @Override
  public void execute(Command cmd) {
    Actor actor = getActorByIdOutPort.query(cmd.actorId()).orElseThrow();
    deleteActorOutPort.delete(actor);
  }
}
