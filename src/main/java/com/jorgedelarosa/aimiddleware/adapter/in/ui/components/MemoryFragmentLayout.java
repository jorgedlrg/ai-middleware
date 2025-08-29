package com.jorgedelarosa.aimiddleware.adapter.in.ui.components;

import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetMemoryUseCase;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.time.Instant;
import java.util.UUID;

/**
 * @author jorge
 */
public class MemoryFragmentLayout extends VerticalLayout {

  private final UUID actor;
  private final UUID id;
  private final TextArea text;
  private final Checkbox enabled;
  private final Instant timestamp;

  public MemoryFragmentLayout(UUID actor, GetMemoryUseCase.MemoryFragmentDto dto) {
    super();
    this.actor = actor;
    this.id = dto.id();
    this.timestamp = dto.timestamp();

    enabled = new Checkbox();
    enabled.setLabel("Enabled");
    enabled.setValue(dto.enabled());
    HorizontalLayout ops = new HorizontalLayout();
    DeleteConfirmButton delete =
        new DeleteConfirmButton("Delete", "memory fragment", deleteFragmentListener());
    ops.add(enabled, delete);

    text = new TextArea();
    text.setWidthFull();
    text.setValue(dto.text());

    addClassNames(LumoUtility.Border.ALL);
    add(ops);
    add(text);
  }

  public UUID getFragmentId() {
    return id;
  }

  public String getText() {
    return text.getValue();
  }

  public boolean getEnabled() {
    return enabled.getValue();
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  private ComponentEventListener<ConfirmDialog.ConfirmEvent> deleteFragmentListener() {
    return (ConfirmDialog.ConfirmEvent t) -> {
      // TODO: call use case
      t.getSource().getUI().ifPresent(ui -> ui.navigate("actors/" + actor + "/memory"));
      Notification notification = Notification.show("Memory fragment deleted!");
      notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
    };
  }
}
