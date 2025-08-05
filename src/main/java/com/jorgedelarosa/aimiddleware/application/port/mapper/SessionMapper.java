package com.jorgedelarosa.aimiddleware.application.port.mapper;

import com.jorgedelarosa.aimiddleware.application.port.in.session.CreateSessionUseCase;
import com.jorgedelarosa.aimiddleware.domain.session.Performance;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.jorgedelarosa.aimiddleware.application.port.in.session.EditSessionUseCase;

/**
 * @author jorge
 */
@Mapper
public interface SessionMapper {
  SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

  Performance toDom(CreateSessionUseCase.PerformanceDto dto);
  
  Performance toDom(EditSessionUseCase.PerformanceDto dto);
}
