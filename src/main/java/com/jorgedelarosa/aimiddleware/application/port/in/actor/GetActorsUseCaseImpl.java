package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import com.jorgedelarosa.aimiddleware.application.port.mapper.ActorMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorsOutPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
public class GetActorsUseCaseImpl implements GetActorsUseCase {

  private final GetActorsOutPort getActorsOutPort;

  @Override
  public List<ActorDto> execute() {
    return getActorsOutPort.query().stream().map(e -> ActorMapper.INSTANCE.toDto(e)).toList();
  }
}
