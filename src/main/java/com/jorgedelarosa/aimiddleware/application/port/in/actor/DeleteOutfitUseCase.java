package com.jorgedelarosa.aimiddleware.application.port.in.actor;

import com.jorgedelarosa.aimiddleware.domain.AggregateRoot;
import com.jorgedelarosa.aimiddleware.domain.DomainEvent;
import java.util.UUID;

/**
 * @author jorge
 */
public interface DeleteOutfitUseCase {
  public void execute(Command cmd);

  public record Command(UUID id) {}

  public class OutfitDeletedEvent extends DomainEvent {

    public OutfitDeletedEvent(AggregateRoot.AggregateId aggregateId, Long version) {
      super(aggregateId, version);
    }

    @Override
    public int getSchemaVersion() {
      return 1;
    }
  }
}
