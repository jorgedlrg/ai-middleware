package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import com.jorgedelarosa.aimiddleware.application.port.mapper.MemoryMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.GetMemoryByActorOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Memory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
public class GetMemoryUseCaseImpl implements GetMemoryUseCase {

  private final GetMemoryByActorOutPort getMemoryByActorOutPort;

  @Override
  public MemoryDto execute(Command cmd) {
    Memory memory = getMemoryByActorOutPort.query(cmd.actorId());

    return MemoryMapper.INSTANCE.map(memory);
  }
}
