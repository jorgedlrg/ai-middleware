package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import com.jorgedelarosa.aimiddleware.application.port.out.GetMemoryByActorOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.SaveMemoryOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Memory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
@Transactional
public class DeleteMemoryFragmentUseCaseImpl implements DeleteMemoryFragmentUseCase {

  private final GetMemoryByActorOutPort getMemoryByActorOutPort;
  private final SaveMemoryOutPort saveMemoryOutPort;

  @Override
  public void execute(Command cmd) {
    Memory memory = getMemoryByActorOutPort.query(cmd.id());
    memory.deleteFragment(cmd.fragmentId());
    saveMemoryOutPort.save(memory);
  }
}
