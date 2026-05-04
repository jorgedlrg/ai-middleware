package com.jorgedelarosa.aimiddleware.adapter.in.ui.infra;

import com.jorgedelarosa.aimiddleware.adapter.out.message.EventEnvelope;
import com.jorgedelarosa.aimiddleware.domain.DomainEvent;

/**
 * @author jorge
 */
public interface EventAware {
  public void handleMessage(EventEnvelope<? extends DomainEvent> envelope);
}
