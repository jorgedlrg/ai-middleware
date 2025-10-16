package com.jorgedelarosa.aimiddleware.adapter.in.ui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.markdown.Markdown;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.server.streams.UploadMetadata;
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

  private void render() {
    removeAll();

    Upload upload =
        new Upload(
            UploadHandler.inMemory(
                (UploadMetadata metadata, byte[] data) -> {
                  card = new CharacterCardReader().read(data);
                }));

    upload.setDropAllowed(true);
    upload.setMaxFiles(1);
    upload.setAcceptedFileTypes("image/png", ".png");
    add(upload);

    Button readDataButton = new Button("Read character", readDataListener());
    add(readDataButton);

    if (card != null) {
      Markdown markdown = new Markdown(card.toString());
      add(markdown);
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
