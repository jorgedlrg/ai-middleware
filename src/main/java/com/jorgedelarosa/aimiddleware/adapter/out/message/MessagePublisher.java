package com.jorgedelarosa.aimiddleware.adapter.out.message;

import com.jorgedelarosa.aimiddleware.adapter.out.OutboxEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
public class MessagePublisher {
  private final ApplicationEventPublisher eventPublisher;

  public MessagePublisher(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  public void publishMessage(OutboxEvent outboxEvent) {
    eventPublisher.publishEvent(outboxEvent);
  }
}
