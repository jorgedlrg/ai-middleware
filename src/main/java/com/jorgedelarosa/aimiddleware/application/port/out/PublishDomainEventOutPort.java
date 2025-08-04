package com.jorgedelarosa.aimiddleware.application.port.out;

import com.jorgedelarosa.aimiddleware.domain.DomainEvent;

/**
 * @author jorge
 */
public interface PublishDomainEventOutPort {
  void publishDomainEvent(DomainEvent event);
}
