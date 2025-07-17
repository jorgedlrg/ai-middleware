package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.ActorEditorActorLayout;
import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.DeleteConfirmButton;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.DeleteActorUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetOutfitsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.SaveActorUseCase;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

/**
 * @author jorge
 */
@Route(value = "actors", layout = MainView.class)
@RequiredArgsConstructor
public class ActorEditorView extends VerticalLayout
    implements HasDynamicTitle, HasUrlParameter<String> {

  private final GetActorDetailsUseCase getActorDetailsUseCase;
  private final SaveActorUseCase saveActorUseCase;
  private final DeleteActorUseCase deleteActorUseCase;
  private final GetOutfitsUseCase getOutfitsUseCase;

  private GetActorDetailsUseCase.ActorDto actorDto;
  private String pageTitle;

  private ActorEditorActorLayout actorEditorActorLayout;

  private void rebuildEditor() {
    removeAll();

    actorEditorActorLayout =
        new ActorEditorActorLayout(
            actorDto, getOutfitsUseCase.execute(new GetOutfitsUseCase.Command()));

    Button saveButton = new Button("Save");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.addClickListener(saveActorListener());
    DeleteConfirmButton deleteButton =
        new DeleteConfirmButton("Delete", actorDto.name(), deleteActorListener());

    add(actorEditorActorLayout);
    add(new Div(saveButton, deleteButton));
  }

  private ComponentEventListener<ClickEvent<Button>> saveActorListener() {
    return (ClickEvent<Button> t) -> {
      saveActorUseCase.execute(
          new SaveActorUseCase.Command(
              actorDto.id(),
              actorEditorActorLayout.getNameValue(),
              actorEditorActorLayout.getPhysicalDescriptionValue(),
              actorEditorActorLayout.getPersonalityValue(),
              actorEditorActorLayout.getPortraitBytes(),
              actorEditorActorLayout.getOutfitValue()));
      t.getSource().getUI().ifPresent(ui -> ui.navigate("actors-list"));
      Notification notification =
          Notification.show(actorEditorActorLayout.getNameValue() + " saved!");
      notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    };
  }

  private ComponentEventListener<ConfirmDialog.ConfirmEvent> deleteActorListener() {
    return (ConfirmDialog.ConfirmEvent t) -> {
      deleteActorUseCase.execute(new DeleteActorUseCase.Command(actorDto.id()));
      t.getSource().getUI().ifPresent(ui -> ui.navigate("actors-list"));
      Notification notification = Notification.show(actorDto.name() + " deleted!");
      notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
    };
  }

  @Override
  public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
    if (parameter != null) {
      actorDto =
          getActorDetailsUseCase.execute(
              new GetActorDetailsUseCase.Command(UUID.fromString(parameter)));
      pageTitle = "Actor Editor - " + actorDto.name();
    } else {
      actorDto =
          new GetActorDetailsUseCase.ActorDto(
              null, "", "", Optional.empty(), new byte[0], Optional.empty());
      pageTitle = "Actor Editor - new";
    }
    rebuildEditor();
  }

  @Override
  public String getPageTitle() {
    return pageTitle;
  }
}
