package com.jorgedelarosa.aimiddleware.adapter.in.ui.components;

import com.jorgedelarosa.aimiddleware.application.port.in.actor.DeleteActorUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorsUseCase;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.theme.lumo.LumoIcon;
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
    setTitle(new Div(dto.name()));

    Image portrait;
    if (dto.portrait() != null && dto.portrait().length > 0) {
      portrait = new ByteImage("Portrait", dto.portrait());
      setMedia(portrait);
    } else {
      Icon icon = LumoIcon.PHOTO.create();
      icon.getStyle()
          .setColor("var(--lumo-primary-color)")
          .setBackgroundColor("var(--lumo-primary-color-10pct)");
      setMedia(icon);
    }

    Button editActor = new Button("Edit");
    editActor.addClickListener(editActorListener());
    DeleteConfirmButton deleteButton =
        new DeleteConfirmButton("Delete", dto.name(), deleteActorListener());
    addToFooter(editActor, deleteButton);
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
}
