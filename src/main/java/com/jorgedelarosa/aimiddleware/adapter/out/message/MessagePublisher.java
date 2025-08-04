package com.jorgedelarosa.aimiddleware.adapter.out.message;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
@RequiredArgsConstructor
public class MessagePublisher {

  // This could be replaced by bindings to external message brokers. Here I use Spring application
  // events to don't use an external message broker and make the deployment simple.
  private final ApplicationEventPublisher eventPublisher;

  // TODO: this is a synchronous publishing. do it asynchronous
  public void publishMessage(EventEnvelope envelope) {
    eventPublisher.publishEvent(envelope);
  }
}
