package com.jorgedelarosa.aimiddleware.application.port.mapper;

import com.jorgedelarosa.aimiddleware.adapter.out.persistence.jpa.MemoryFragmentEntity;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetMemoryUseCase;
import com.jorgedelarosa.aimiddleware.domain.actor.Memory;
import com.jorgedelarosa.aimiddleware.domain.actor.MemoryFragment;
import java.time.Instant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author jorge
 */
@Mapper
public interface MemoryMapper {

  MemoryMapper INSTANCE = Mappers.getMapper(MemoryMapper.class);

  default MemoryFragment map(MemoryFragmentEntity entity) {
    return MemoryFragment.restore(
        entity.getText(),
        Instant.ofEpochMilli(entity.getTimestamp()),
        entity.getId(),
        entity.isEnabled());
  }

  @Mapping(target = "actorId", source = "actor")
  GetMemoryUseCase.MemoryDto map(Memory dom);
}
