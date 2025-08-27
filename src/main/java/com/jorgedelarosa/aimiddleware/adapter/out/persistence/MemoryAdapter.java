package com.jorgedelarosa.aimiddleware.adapter.out.persistence;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.MemoryFragmentRepository;
import com.jorgedelarosa.aimiddleware.application.port.mapper.MemoryMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.GetMemoryByActorOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Memory;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
public class MemoryAdapter implements GetMemoryByActorOutPort {

  private final MemoryFragmentRepository memoryFragmentRepository;

  @Override
  public Memory query(UUID actor) {
    //TODO: persist Memory aggregate or refine it, I'm not sure about it
    return Memory.restore(
        actor,
        memoryFragmentRepository.findAllByOwner(actor).stream()
            .map(e -> MemoryMapper.INSTANCE.map(e))
            .toList(),
        actor);
  }
}
