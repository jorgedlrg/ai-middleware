package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import com.jorgedelarosa.aimiddleware.application.port.out.SaveMemoryOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Memory;
import com.jorgedelarosa.aimiddleware.domain.actor.MemoryFragment;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
@Transactional
public class SaveMemoryUseCaseImpl implements SaveMemoryUseCase {

  private final SaveMemoryOutPort saveMemoryOutPort;

  @Override
  public UUID execute(Command cmd) {
    saveMemoryOutPort.save(
        Memory.restore(
            cmd.actorId(),
            cmd.fragments().stream()
                .map(
                    e -> {
                      if (e.id() != null) {
                        return MemoryFragment.restore(e.text(), e.timestamp(), e.id(), e.enabled());
                      } else {
                        return MemoryFragment.create(e.text());
                      }
                    })
                .toList(),
            cmd.actorId()));

    return cmd.actorId();
  }
}
