package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import com.jorgedelarosa.aimiddleware.application.port.mapper.ActorMapper;
import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
public class GetActorDetailsUseCaseImpl implements GetActorDetailsUseCase {
  private final GetActorByIdOutPort getActorByIdOutPort;

  @Override
  public ActorDto execute(Command cmd) {
    // TODO: catch all these NoSuchElementExceptions
    return getActorByIdOutPort
        .query(cmd.actorId())
        .map(e -> ActorMapper.INSTANCE.toDetailDto(e))
        .orElseThrow();
  }
}
