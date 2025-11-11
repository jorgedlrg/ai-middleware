package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import com.jorgedelarosa.aimiddleware.application.port.out.DeleteActorOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.PublishDomainEventOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.actor.ActorDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
@Transactional
public class DeleteActorUseCaseImpl implements DeleteActorUseCase {

  private final GetActorByIdOutPort getActorByIdOutPort;
  private final DeleteActorOutPort deleteActorOutPort;
  private final PublishDomainEventOutPort publishDomainEventOutPort;

  @Override
  public void execute(Command cmd) {
    Actor actor = getActorByIdOutPort.query(cmd.actorId()).orElseThrow();
    deleteActorOutPort.delete(actor);
    publishDomainEventOutPort.publishDomainEvent(new ActorDeletedEvent(actor.getAggregateId(), 1l));
  }
}
