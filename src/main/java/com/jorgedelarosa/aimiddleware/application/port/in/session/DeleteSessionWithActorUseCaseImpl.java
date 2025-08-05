package com.jorgedelarosa.aimiddleware.application.port.in.session;

import com.jorgedelarosa.aimiddleware.application.port.out.DeleteSessionOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionsOutPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
@Transactional
public class DeleteSessionWithActorUseCaseImpl implements DeleteSessionWithActorUseCase {

  private final GetSessionsOutPort getSessionsOutPort;
  private final DeleteSessionOutPort deleteSessionOutPort;

  @Override
  public void execute(Command cmd) {
    getSessionsOutPort.query().stream()
        .filter(s -> s.getPerformances().stream().anyMatch(p -> p.getActor().equals(cmd.actor())))
        .forEach(s -> deleteSessionOutPort.delete(s));
  }
}
