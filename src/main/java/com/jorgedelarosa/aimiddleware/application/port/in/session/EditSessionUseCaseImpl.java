package com.jorgedelarosa.aimiddleware.application.port.in.session;

import com.jorgedelarosa.aimiddleware.application.port.mapper.SessionMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveSessionOutPort;
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
public class EditSessionUseCaseImpl implements EditSessionUseCase {

  private final GetSessionByIdOutPort getSessionByIdOutPort;
  private final SaveSessionOutPort saveSessionOutPort;

  @Override
  public void execute(Command cmd) {
    Session session = getSessionByIdOutPort.query(cmd.session()).orElseThrow();

    session.replacePerformances(
        cmd.performances().stream().map(p -> SessionMapper.INSTANCE.toDom(p)).toList());
    session.setCurrentContext(cmd.currentContext());
    session.setLocale(cmd.locale());

    saveSessionOutPort.save(session);
  }
}
