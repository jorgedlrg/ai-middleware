package com.jorgedelarosa.aimiddleware.application.port.in.session;

import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.PublishDomainEventOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
import com.jorgedelarosa.aimiddleware.domain.session.InteractionAddedEvent;
import com.jorgedelarosa.aimiddleware.domain.session.InteractionText;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
@Transactional
public class UserInteractUseCaseImpl implements UserInteractUseCase {

  private final GetSessionByIdOutPort getSessionByIdOutPort;
  private final SaveSessionOutPort saveSessionOutPort;
  private final PublishDomainEventOutPort publishDomainEventOutPort;

  @Override
  public void execute(Command cmd) {
    Session session = getSessionByIdOutPort.query(cmd.session()).orElseThrow();

    // TODO: REFINE. maybe make the user send actions and thoughts, if necessary
    session.interact(
        Optional.empty(),
        Optional.empty(),
        new InteractionText(cmd.text(), Optional.empty()),
        cmd.role(),
        Optional.empty());

    saveSessionOutPort.save(session);
    publishDomainEventOutPort.publishDomainEvent(
        new InteractionAddedEvent(
            session.getAggregateId(), 1l, session.getLastInteraction(), cmd.autoreplyRole()));
  }
}
