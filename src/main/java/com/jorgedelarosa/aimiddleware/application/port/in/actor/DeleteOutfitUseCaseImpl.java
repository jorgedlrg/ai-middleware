package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import com.jorgedelarosa.aimiddleware.application.port.out.DeleteOutfitOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.GetOutfitByIdOutPort;
import com.jorgedelarosa.aimiddleware.application.port.out.PublishDomainEventOutPort;
import com.jorgedelarosa.aimiddleware.domain.actor.Outfit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
@Transactional
public class DeleteOutfitUseCaseImpl implements DeleteOutfitUseCase {

  private final GetOutfitByIdOutPort getOutfitByIdOutPort;
  private final DeleteOutfitOutPort deleteOutfitOutPort;
  private final PublishDomainEventOutPort publishDomainEventOutPort;

  @Override
  public void execute(Command cmd) {
    Outfit outfit = getOutfitByIdOutPort.query(cmd.id()).orElseThrow();
    deleteOutfitOutPort.delete(outfit);
    publishDomainEventOutPort.publishDomainEvent(
        new DeleteOutfitUseCase.OutfitDeletedEvent(outfit.getAggregateId(), 1l));
  }
}
