package com.jorgedelarosa.aimiddleware.application.port.in.session;

import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
import com.jorgedelarosa.aimiddleware.domain.session.InteractionText;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
@Transactional
public class UserInteractUseCaseImpl implements UserInteractUseCase {

  private final GetSessionByIdOutPort getSessionByIdOutPort;
  private final SaveSessionOutPort saveSessionOutPort;

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
  }
}
