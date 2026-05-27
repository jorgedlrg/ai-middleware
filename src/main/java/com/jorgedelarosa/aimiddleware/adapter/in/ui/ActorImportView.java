package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.jorgedelarosa.aimiddleware.adapter.in.ui.CharacterCardReader.CharacterCardV2;
import com.jorgedelarosa.aimiddleware.adapter.in.ui.components.ActorEditorActorLayout;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.GetActorDetailsUseCase;
import com.jorgedelarosa.aimiddleware.application.port.in.actor.ImportCharacterCardUseCase;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.server.streams.UploadMetadata;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Route(value = "actors-import", layout = MainView.class)
@PageTitle("Actor import")
@RequiredArgsConstructor
@Slf4j
public class ActorImportView extends VerticalLayout implements BeforeEnterObserver {

  private final ImportCharacterCardUseCase importCharacterCardUseCase;

  private CharacterCardV2 card;
  private byte[] portraitBytes;
  private ActorEditorActorLayout actorEditorLayout;

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
      String processedDescription =
          card.data().description().replace("{{char}}", card.data().name());
      actorEditorLayout =
          new ActorEditorActorLayout(
              new GetActorDetailsUseCase.ActorDto(
                  null,
                  card.data().name(),
                  processedDescription,
                  processedDescription,
                  Optional.of(new GetActorDetailsUseCase.MindDto(card.data().personality())),
                  Optional.empty()),
              Collections.EMPTY_LIST);

      add(actorEditorLayout);

      Button importButton = new Button("Import Character");
      importButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
      importButton.addClickListener(importCharacterListener());
      add(importButton);

      addInfoSection();
    }
  }

  private void addInfoSection() {
    add(new H3("Card data (not persisted — for reference only)"));

    addReadOnlyTextArea("Scenario", card.data().scenario());
    addReadOnlyTextArea("First message", card.data().first_mes());
    addReadOnlyTextArea("Message example", card.data().mes_example());
    addReadOnlyTextArea("System prompt", card.data().system_prompt());
    addReadOnlyTextArea("Post history instructions", card.data().post_history_instructions());

    if (card.data().alternate_greetings() != null && !card.data().alternate_greetings().isEmpty()) {
      int i = 1;
      for (String greeting : card.data().alternate_greetings()) {
        addReadOnlyTextArea("Alt Greeting " + i, greeting);
        i++;
      }
    }

    addReadOnlyTextField("Creator notes", card.data().creator_notes());
    addReadOnlyTextField("Creator", card.data().creator());
    addReadOnlyTextField("Character version", card.data().character_version());

    if (card.data().tags() != null && !card.data().tags().isEmpty()) {
      addReadOnlyTextField("Tags", String.join(", ", card.data().tags()));
    }
  }

  private void addReadOnlyTextArea(String label, String value) {
    if (value == null || value.isBlank()) return;
    TextArea area = new TextArea(label);
    area.setValue(value);
    area.setReadOnly(true);
    area.setWidthFull();
    area.setMinRows(2);
    add(area);
  }

  private void addReadOnlyTextField(String label, String value) {
    if (value == null || value.isBlank()) return;
    TextField field = new TextField(label);
    field.setValue(value);
    field.setReadOnly(true);
    field.setWidthFull();
    add(field);
  }

  private ComponentEventListener<ClickEvent<Button>> readDataListener() {
    return (ClickEvent<Button> t) -> {
      render();
    };
  }

  private ComponentEventListener<ClickEvent<Button>> importCharacterListener() {
    return (ClickEvent<Button> t) -> {
      List<String> altGreetings =
          card.data().alternate_greetings() != null ? card.data().alternate_greetings() : List.of();
      importCharacterCardUseCase.execute(
          new ImportCharacterCardUseCase.Command(
              actorEditorLayout.getNameValue(),
              actorEditorLayout.getProfileValue(),
              actorEditorLayout.getPersonalityValue(),
              card.data().scenario(),
              card.data().first_mes(),
              altGreetings,
              portraitBytes));
      t.getSource().getUI().ifPresent(ui -> ui.navigate("actors-list"));
      Notification notification =
          Notification.show(actorEditorLayout.getNameValue() + " imported!");
      notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    };
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    render();
  }
}
