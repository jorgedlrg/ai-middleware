package com.jorgedelarosa.aimiddleware.adapter.out;

import org.springframework.context.ApplicationEvent;

/**
 * @author jorge
 */
public class OutboxEvent extends ApplicationEvent {
  private final String payload;

  public OutboxEvent(Object source, String payload) {
    super(source);
    this.payload = payload;
  }

  public String getPayload() {
    return payload;
  }
}
