package com.jorgedelarosa.aimiddleware.application.port.in.session;

import com.jorgedelarosa.aimiddleware.application.port.out.DeleteSessionOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
@Transactional
public class DeleteSessionUseCaseImpl implements DeleteSessionUseCase {

  private final GetSessionByIdOutPort getSessionByIdOutPort;
  private final DeleteSessionOutPort deleteSessionOutPort;

  @Override
  public void execute(Command cmd) {
    Session session = getSessionByIdOutPort.query(cmd.sessionId()).orElseThrow();
    deleteSessionOutPort.delete(session);
  }
}
