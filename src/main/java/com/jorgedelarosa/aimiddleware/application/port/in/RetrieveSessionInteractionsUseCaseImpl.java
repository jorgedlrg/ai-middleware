package com.jorgedelarosa.aimiddleware.application.port.in;

import com.jorgedelarosa.aimiddleware.application.port.out.GetSessionByIdOutPort;
import com.jorgedelarosa.aimiddleware.domain.session.Interaction;
import com.jorgedelarosa.aimiddleware.domain.session.Session;
import java.util.List;
import java.util.UUID;
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

  @Override
  public List<InteractionDto> execute(Command cmd) {
    Session session =
        getSessionByIdOutPort
            .query(UUID.fromString("7376f89d-4ca7-423b-95f1-e29a8832ec4a"))
            .orElseThrow(); // FIXME

    return session.getInteractions().stream()
        .map((e) -> InteractionMapper.INSTANCE.toDto(e))
        .toList();
  }

  @Mapper
  public interface InteractionMapper {
    InteractionMapper INSTANCE = Mappers.getMapper(InteractionMapper.class);

    InteractionDto toDto(Interaction dom);
  }
}
