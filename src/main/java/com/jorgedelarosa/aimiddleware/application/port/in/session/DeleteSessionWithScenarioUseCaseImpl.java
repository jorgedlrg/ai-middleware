package com.jorgedelarosa.aimiddleware.application.port.in.session;

import com.jorgedelarosa.aimiddleware.application.port.out.DeleteSessionOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionsByScenarioOutPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
@Transactional
public class DeleteSessionWithScenarioUseCaseImpl implements DeleteSessionWithScenarioUseCase {

  private final GetSessionsByScenarioOutPort getSessionsByScenarioOutPort;
  private final DeleteSessionOutPort deleteSessionOutPort;

  @Override
  public void execute(Command cmd) {
    getSessionsByScenarioOutPort.queryByScenario(cmd.scenario()).stream()
        .forEach(s -> deleteSessionOutPort.delete(s));
  }
}
