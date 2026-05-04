package com.jorgedelarosa.aimiddleware.adapter.in.ui.infra;

import com.jorgedelarosa.aimiddleware.adapter.out.message.EventEnvelope;
import com.jorgedelarosa.aimiddleware.domain.DomainEvent;
import com.jorgedelarosa.aimiddleware.domain.session.InteractionAddedEvent;
import com.vaadin.flow.shared.Registration;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author jorge
 */
@Component
public class InteractionDispatcher {

  private final Set<Consumer<EventEnvelope<? extends DomainEvent>>> listeners =
      new CopyOnWriteArraySet<>();

  @EventListener
  public void onInteractionAdded(EventEnvelope<? extends DomainEvent> envelope) {
    if (envelope.getEvent() instanceof InteractionAddedEvent) {
      listeners.forEach(listener -> listener.accept(envelope));
    }
  }

  public Registration register(Consumer<EventEnvelope<? extends DomainEvent>> listener) {
    listeners.add(listener);
    return () -> listeners.remove(listener);
  }
}
