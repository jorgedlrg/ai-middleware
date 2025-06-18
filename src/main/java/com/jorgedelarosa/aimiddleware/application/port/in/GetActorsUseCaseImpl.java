package com.jorgedelarosa.aimiddleware.application.port.in;

import com.jorgedelarosa.aimiddleware.application.port.out.GetActorsOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import java.util.List;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@AllArgsConstructor
public class GetActorsUseCaseImpl implements GetActorsUseCase {

  private final GetActorsOutPort getActorsOutPort;

  @Override
  public List<ActorDto> execute(Command cmd) {
    return getActorsOutPort.query().stream().map(e -> ActorMapper.INSTANCE.toDto(e)).toList();
  }

  @Mapper
  public interface ActorMapper {
    ActorMapper INSTANCE = Mappers.getMapper(ActorMapper.class);

    GetActorsUseCase.ActorDto toDto(Actor dom);
  }
}
