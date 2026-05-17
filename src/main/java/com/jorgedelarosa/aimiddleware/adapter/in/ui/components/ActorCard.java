package com.jorgedelarosa.aimiddleware.adapter.in.ui.components;

import com.jorgedelarosa.aimiddleware.application.port.in.actor.DeleteActorUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorsUseCase;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.UUID;

/**
 * @author jorge
 */
public class ActorCard extends Card {

  private final DeleteActorUseCase deleteActorUseCase;
  private final UUID id;
  private final String name;

  public ActorCard(GetActorsUseCase.ActorDto dto, DeleteActorUseCase deleteActorUseCase) {
    super();
    this.deleteActorUseCase = deleteActorUseCase;
    id = dto.id();
    name = dto.name();
    setTitle(name);
    setMaxWidth("432px");

    Image portrait = new Image("/api/v1/actor/actors/" + dto.id() + "/portrait", "Portrait");
    portrait.setMaxWidth("216px");
    portrait.setMinWidth("216px");
    setMedia(portrait);

    Button editActor = new Button("Edit");
    editActor.addClickListener(editActorListener());
    Button memory = new Button("Memory");
    memory.addClickListener(editMemoryListener());
    DeleteConfirmButton deleteButton =
        new DeleteConfirmButton("Delete", name, deleteActorListener());

    VerticalLayout rightPanel = new VerticalLayout(editActor, memory, deleteButton);
    addToFooter(rightPanel);

    addThemeVariants(
        CardVariant.LUMO_HORIZONTAL, CardVariant.LUMO_COVER_MEDIA, CardVariant.LUMO_ELEVATED);
  }

  private ComponentEventListener<ConfirmDialog.ConfirmEvent> deleteActorListener() {
    return (ConfirmDialog.ConfirmEvent t) -> {
      deleteActorUseCase.execute(new DeleteActorUseCase.Command(id));
      t.getSource().getUI().ifPresent(ui -> ui.navigate("actors-list"));
      Notification notification = Notification.show(name + " deleted!");
      notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
    };
  }

  private ComponentEventListener<ClickEvent<Button>> editActorListener() {
    return (ClickEvent<Button> t) -> {
      t.getSource().getUI().ifPresent(ui -> ui.navigate("actors/" + id));
    };
  }

  private ComponentEventListener<ClickEvent<Button>> editMemoryListener() {
    return (ClickEvent<Button> t) -> {
      t.getSource().getUI().ifPresent(ui -> ui.navigate("actors/" + id + "/memory"));
    };
  }
}
