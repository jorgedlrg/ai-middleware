package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.ActorEditorActorLayout;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorDetailsUseCase;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.server.streams.UploadMetadata;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jorge
 */
@Route(value = "actors-import", layout = MainView.class)
@PageTitle("Actor import")
@RequiredArgsConstructor
@Slf4j
public class ActorImportView extends VerticalLayout implements BeforeEnterObserver {

  private CharacterCardReader.CharacterCardV2 card;
  private byte[] portraitBytes;
  private ActorEditorActorLayout actorEditorLayout;
  private TextArea scenario;
  private TextArea firstMes;

  private void render() {
    removeAll();

    Upload upload =
        new Upload(
            UploadHandler.inMemory(
                (UploadMetadata metadata, byte[] data) -> {
                  card = new CharacterCardReader().read(data);
                  portraitBytes = Arrays.copyOf(data, data.length);
                }));

    upload.setDropAllowed(true);
    upload.setMaxFiles(1);
    upload.setAcceptedFileTypes("image/png", ".png");
    add(upload);

    Button readDataButton = new Button("Read character", readDataListener());
    add(readDataButton);

    if (card != null) {
      actorEditorLayout =
          new ActorEditorActorLayout(
              new GetActorDetailsUseCase.ActorDto(
                  null,
                  card.data().name(),
                  card.data().description(),
                  card.data().description(),
                  Optional.of(new GetActorDetailsUseCase.MindDto(card.data().personality())),
                  portraitBytes,
                  Optional.empty()),
              Collections.EMPTY_LIST);
      scenario = new TextArea("Scenario");
      scenario.setValue(card.data().scenario());
      scenario.setWidthFull();
      scenario.setMinRows(4);
      firstMes = new TextArea("Introduction");
      firstMes.setValue(card.data().first_mes());
      firstMes.setWidthFull();
      firstMes.setMinRows(4);
      add(actorEditorLayout);
      add(scenario);
      add(firstMes);
    }
  }

  private ComponentEventListener<ClickEvent<Button>> readDataListener() {
    return (ClickEvent<Button> t) -> {
      render();
    };
  }

  private ComponentEventListener<ClickEvent<Button>> importActorListener() {
    return (ClickEvent<Button> t) -> {
      t.getSource().getUI().ifPresent(ui -> ui.navigate("actors-list"));
      Notification notification = Notification.show("Actor imported!");
      notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    };
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    render();
  }
}
