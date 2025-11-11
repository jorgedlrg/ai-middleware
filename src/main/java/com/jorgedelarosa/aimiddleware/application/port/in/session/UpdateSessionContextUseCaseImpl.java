package com.jorgedelarosa.aimiddleware.application.port.in.session;

import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
@Transactional
public class UpdateSessionContextUseCaseImpl implements UpdateSessionContextUseCase {

  private final GetSessionByIdOutPort getSessionByIdOutPort;
  private final SaveSessionOutPort saveSessionOutPort;

  @Override
  public void execute(UpdateSessionContextUseCase.Command cmd) {
    Session session = getSessionByIdOutPort.query(cmd.id()).orElseThrow();

    session.setCurrentContext(cmd.contextId());

    saveSessionOutPort.save(session);
  }
}
