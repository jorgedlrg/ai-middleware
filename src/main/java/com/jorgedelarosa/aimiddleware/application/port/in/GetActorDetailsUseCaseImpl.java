package com.jorgedelarosa.aimiddleware.application.port.in;

import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.actor.Mind;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
public class GetActorDetailsUseCaseImpl implements GetActorDetailsUseCase {
  private final GetActorByIdOutPort getActorByIdOutPort;

  @Override
  public ActorDto execute(Command cmd) {
    // TODO: catch all these NoSuchElementExceptions
    return getActorByIdOutPort
        .query(cmd.actorId())
        .map(e -> ActorMapper.INSTANCE.toDto(e))
        .orElseThrow();
  }

  @Mapper
  public interface ActorMapper {
    ActorMapper INSTANCE = Mappers.getMapper(ActorMapper.class);

    default Optional<GetActorDetailsUseCase.MindDto> map(Optional<Mind> value) {
      return value.map(e -> new MindDto(e.getPersonality()));
    }

    GetActorDetailsUseCase.ActorDto toDto(Actor dom);
  }
}
