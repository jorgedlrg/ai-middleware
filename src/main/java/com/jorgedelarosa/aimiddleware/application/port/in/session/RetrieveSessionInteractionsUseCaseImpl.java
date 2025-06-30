package com.jorgedelarosa.aimiddleware.application.port.in.session;

import com.jorgedelarosa.aimiddleware.application.port.out.GetActorByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Actor;
import com.jorgedelarosa.aimiddleware.domain.session.Interaction;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.List;
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
public class RetrieveSessionInteractionsUseCaseImpl implements RetrieveSessionInteractionsUseCase {

  private final GetSessionByIdOutPort getSessionByIdOutPort;
  private final GetActorByIdOutPort getActorByIdOutPort;

  @Override
  public List<InteractionDto> execute(Command cmd) {
    Session session = getSessionByIdOutPort.query(cmd.session()).orElseThrow();

    return session.getInteractions().stream()
        .map((e) -> InteractionMapper.INSTANCE.toDto(e, getActorByIdOutPort.query(e.getActor())))
        .toList();
  }

  @Mapper
  public interface InteractionMapper {
    InteractionMapper INSTANCE = Mappers.getMapper(InteractionMapper.class);

    default InteractionDto toDto(Interaction dom, Optional<Actor> actor) {
      return new InteractionDto(
          dom.getId(), dom.getTimestamp(), actor.orElseThrow().getName(), dom.getSpokenText());
    }
  }
}
